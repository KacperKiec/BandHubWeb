package io.kk.bandhub.musicservice.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kk.bandhub.musicservice.model.Genre;
import io.kk.bandhub.musicservice.model.Instrument;
import io.kk.bandhub.musicservice.repository.genre.JpaGenreRepository;
import io.kk.bandhub.musicservice.repository.instrument.JpaInstrumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MusicDataInitializer implements CommandLineRunner {

    private final JpaInstrumentRepository instrumentRepository;
    private final JpaGenreRepository genreRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        if (instrumentRepository.count() == 0) {
            InputStream instrumentStream = new ClassPathResource("/sample_data/instruments.json").getInputStream();
            List<String> instrumentNames = objectMapper.readValue(instrumentStream, new TypeReference<>() {});
            List<Instrument> instruments = instrumentNames.stream()
                    .map(name -> {
                        Instrument instrument = new Instrument();
                        instrument.setName(name);
                        return instrument;
                    })
                    .toList();
            instrumentRepository.saveAll(instruments);
        }

        if (genreRepository.count() == 0) {
            InputStream genreStream = new ClassPathResource("/sample_data/genres.json").getInputStream();
            List<String> genreNames = objectMapper.readValue(genreStream, new TypeReference<>() {});
            List<Genre> genres = genreNames.stream()
                    .map(name -> {
                        Genre genre = new Genre();
                        genre.setName(name);
                        return genre;
                    })
                    .toList();
            genreRepository.saveAll(genres);
        }
    }
}
