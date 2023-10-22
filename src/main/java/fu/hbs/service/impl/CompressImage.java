package fu.hbs.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

public class CompressImage {

	public byte[] compressImage(MultipartFile file) {
		try {
			InputStream inputStream = file.getInputStream();
			BufferedImage originalImage = ImageIO.read(inputStream);

			// Nén ảnh với chất lượng cố định (ví dụ: 0.5 là mức nén 50%)
			BufferedImage compressedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			compressedImage.getGraphics().drawImage(originalImage, 0, 0, null);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(compressedImage, "jpeg", outputStream);

			return outputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
}
