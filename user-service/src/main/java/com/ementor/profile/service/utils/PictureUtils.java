/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class PictureUtils {

	private PictureUtils() {
	}
	public static byte[] getPreparedIamge(byte[] originalBytes,
			Integer desiredWidth,
			Integer desiredHeight) throws IOException {
		BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalBytes));
		originalImage = getResizedImage(originalImage, desiredWidth, desiredHeight);
		return compressImage(originalImage);
	}

	private static BufferedImage getResizedImage(BufferedImage originalImage,
			Integer desiredWidth,
			Integer desiredHeight) {
		int originalWidth = originalImage.getWidth();
		int originalHeight = originalImage.getHeight();

		if (originalWidth > desiredWidth || originalHeight > desiredHeight) {
			Dimension boundary = new Dimension(desiredWidth, desiredHeight);
			Dimension newSize = getScaledDimension(new Dimension(originalWidth, originalHeight), boundary);
			originalImage = resizeImage(originalImage, newSize.width, newSize.height);
		}
		return originalImage;
	}

	public static Dimension getScaledDimension(Dimension imgSize,
			Dimension boundary) {
		double widthRatio = boundary.getWidth() / imgSize.getWidth();
		double heightRatio = boundary.getHeight() / imgSize.getHeight();
		double ratio = Math.min(widthRatio, heightRatio);

		return new Dimension((int) (imgSize.width * ratio), (int) (imgSize.height * ratio));
	}

	public static BufferedImage resizeImage(BufferedImage originalImage,
			int targetWidth,
			int targetHeight) {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		graphics2D.dispose();

		return resizedImage;
	}

	private static byte[] compressImage(BufferedImage originalImage) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, "jpg", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();

		if (imageInByte.length > 1024 * 1024) { // If size is larger than 1MB
			float compressionQuality = 0.5f; // Change this to adjust the
			// quality of the image
			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
			ImageWriter writer = writers.next();

			ByteArrayOutputStream compressedBaos = new ByteArrayOutputStream();
			ImageOutputStream ios = ImageIO.createImageOutputStream(compressedBaos);
			writer.setOutput(ios);

			ImageWriteParam param = writer.getDefaultWriteParam();

			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(compressionQuality);
			writer.write(null, new IIOImage(originalImage, null, null), param);

			ios.flush();
			ios.close();
			writer.dispose();
			compressedBaos.flush();
			imageInByte = compressedBaos.toByteArray();
			compressedBaos.close();
		}

		return imageInByte;
	}
}
