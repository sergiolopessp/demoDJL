package com.example.demoDjl;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.ImageFactory;

import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import ai.djl.modality.cv.Image;

import java.io.IOException;
import java.util.function.Supplier;

import javax.annotation.Resource;

@SpringBootApplication
public class DemoDjlApplication implements CommandLineRunner {
	private static final Logger LOG = LoggerFactory.getLogger(DemoDjlApplication.class);

	@Resource
	private Supplier<Predictor<Image, DetectedObjects>> predictorProvider;

	public static void main(String[] args) {
		SpringApplication.run(DemoDjlApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var imagens = new org.springframework.core.io.Resource[] {
			new ClassPathResource("/como-tirar-foto-de-cachorro.jpg"),
				new ClassPathResource("/1307576.jpg"),
				new ClassPathResource("/693450.jpg")

		};

		for(var imagem: imagens) {
			rodarInferencia(imagem);
		}
	}

	private void rodarInferencia(org.springframework.core.io.Resource imagem) throws IOException, TranslateException {
		Image image = ImageFactory.getInstance().fromInputStream(imagem.getInputStream());

		try (var predictor = predictorProvider.get()) {
			var resultados = predictor.predict(image);

			var items = resultados.items();

			for (var resultado : items) {
				LOG.info("Resultados para {}: {}", imagem.getFilename(), resultado.toString());
			}
		}
	}
}
