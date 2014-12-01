package pl.touk.sputnik.processor.sonar;

import java.io.File;

import lombok.extern.slf4j.Slf4j;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import pl.touk.sputnik.review.Review;
import pl.touk.sputnik.review.ReviewProcessor;
import pl.touk.sputnik.review.ReviewResult;

@Slf4j
public class SonarProcessor implements ReviewProcessor {

    private static final String PROCESSOR_NAME = "Sonar";

    private final SonarRunnerBuilder sonarRunnerBuilder = new SonarRunnerBuilder();

    @Nullable
    @Override
    public ReviewResult process(@NotNull Review review) {
        if (noFilesToReview(review)) {
            return new ReviewResult();
        }
        SonarRunner sonarRunner = sonarRunnerBuilder.prepareRunner(review);
        sonarRunner.run();
        // Results results = sonarRunner.execute();

        ResultParser parser = new ResultParser(new File("./.sonar/sonar-report.json"));

        return parser.parseResults();
    }

    private boolean noFilesToReview(Review review) {
        return review.getFiles().isEmpty();
    }


    @NotNull
    @Override
    public String getName() {
        return PROCESSOR_NAME;
    }
}
