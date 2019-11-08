/*
 * © Copyright
 *
 * VideoService.java is part of shashkiserver.
 *
 * shashkiserver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * shashkiserver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with shashkiserver.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.workingbit.shashkiapp.service;

import io.humble.video.*;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.List;

@Service
public class VideoService {

  /**
   * Convert a {@link BufferedImage} of any type, to {@link BufferedImage} of a
   * specified type. If the source image is the same type as the target type,
   * then original image is returned, otherwise new image of the correct type is
   * created and the content of the source image is copied into the new image.
   *
   * @param sourceImage the image to be converted
   * @param targetType  the desired BufferedImage type
   * @return a BufferedImage of the specifed target type.
   * @see BufferedImage
   */

  public static BufferedImage convertToType(BufferedImage sourceImage,
                                            int targetType) {
    BufferedImage image;

    // if the source image is already the target type, return the source image

    if (sourceImage.getType() == targetType)
      image = sourceImage;

      // otherwise create a new image of the target type and draw the new
      // image

    else {
      image = new BufferedImage(sourceImage.getWidth(),
          sourceImage.getHeight(), targetType);
      image.getGraphics().drawImage(sourceImage, 0, 0, null);
    }

    return image;
  }

  public InputStream createVideo(List<String> images, String formatName, int snapsPerSecond) throws IOException, InterruptedException {
    if (StringUtils.isBlank(formatName)) {
      formatName = "mp4";
    }
    String codecName = null;
    Base64.Decoder decoder = Base64.getDecoder();
    byte[] pic = decoder.decode(images.get(0).split(",")[1]);
    InputStream in = new ByteArrayInputStream(pic);
    BufferedImage bufferedImage = ImageIO.read(in);

    Rectangle screenBounds = new Rectangle(bufferedImage.getWidth(), bufferedImage.getHeight());
    File tempFile = File.createTempFile("video-", "." + formatName);
    tempFile.deleteOnExit();

    final Rational framerate = Rational.make(1, snapsPerSecond);

    /** First we create a muxer using the passed in filename and formatname if given. */
    final Muxer muxer = Muxer.make(tempFile.getAbsolutePath(), null, formatName);

    final MuxerFormat format = muxer.getFormat();
    final Codec codec;
    if (codecName != null) {
      codec = Codec.findEncodingCodecByName(codecName);
    } else {
      codec = Codec.findEncodingCodec(format.getDefaultVideoCodecId());
    }

    /**
     * Now that we know what codec, we need to create an encoder
     */
    Encoder encoder = Encoder.make(codec);

    /**
     * Video encoders need to know at a minimum:
     *   width
     *   height
     *   pixel format
     * Some also need to know frame-rate (older codecs that had a fixed rate at which video files could
     * be written needed this). There are many other options you can set on an encoder, but we're
     * going to keep it simpler here.
     */
    encoder.setWidth(screenBounds.width);
    encoder.setHeight(screenBounds.height);
    // We are going to use 420P as the format because that's what most video formats these days use
    final PixelFormat.Type pixelformat = PixelFormat.Type.PIX_FMT_YUV420P;
    encoder.setPixelFormat(pixelformat);
    encoder.setTimeBase(framerate);

    /** An annoynace of some formats is that they need global (rather than per-stream) headers,
     * and in that case you have to tell the encoder. And since Encoders are decoupled from
     * Muxers, there is no easy way to know this beyond
     */
    if (format.getFlag(MuxerFormat.Flag.GLOBAL_HEADER))
      encoder.setFlag(Encoder.Flag.FLAG_GLOBAL_HEADER, true);

    /** Open the encoder. */
    encoder.open(null, null);


    /** Add this stream to the muxer. */
    muxer.addNewStream(encoder);

    /** And open the muxer for business. */
    muxer.open(null, null);

    /** Next, we need to make sure we have the right MediaPicture format objects
     * to encode data with. Java (and most on-screen graphics programs) use some
     * variant of Red-Green-Blue image encoding (a.k.a. RGB or BGR). Most video
     * codecs use some variant of YCrCb formatting. So we're going to have to
     * convert. To do that, we'll introduce a MediaPictureConverter object later. object.
     */
    MediaPictureConverter converter = null;
    final MediaPicture picture = MediaPicture.make(
        encoder.getWidth(),
        encoder.getHeight(),
        pixelformat);
    picture.setTimeBase(framerate);

    /** Now begin our main loop of taking screen snaps.
     * We're going to encode and then write out any resulting packets. */
    final MediaPacket packet = MediaPacket.make();
    int i = 0;
    for (String image : images) {
      /** Make the screen capture && convert image to TYPE_3BYTE_BGR */
      decoder = Base64.getDecoder();
      pic = decoder.decode(image.split(",")[1]);
      in = new ByteArrayInputStream(pic);
      bufferedImage = ImageIO.read(in);
      final BufferedImage screen = convertToType(bufferedImage, BufferedImage.TYPE_3BYTE_BGR);

      /** This is LIKELY not in YUV420P format, so we're going to convert it using some handy utilities. */
      if (converter == null)
        converter = MediaPictureConverterFactory.createConverter(screen, picture);
      converter.toPicture(picture, screen, i);

      do {
        encoder.encode(packet, picture);
        if (packet.isComplete())
          muxer.write(packet, false);
      } while (packet.isComplete());

      /** now we'll sleep until it's time to take the next snapshot. */
      Thread.sleep((long) (1000 * framerate.getDouble()));
      i++;
    }

    /** Encoders, like decoders, sometimes cache pictures so it can do the right key-frame optimizations.
     * So, they need to be flushed as well. As with the decoders, the convention is to pass in a null
     * input until the output is not complete.
     */
    do {
      encoder.encode(packet, null);
      if (packet.isComplete())
        muxer.write(packet, false);
    } while (packet.isComplete());

    /** Finally, let's clean up after ourselves. */
    muxer.close();
    return new FileInputStream(tempFile);
  }
}
