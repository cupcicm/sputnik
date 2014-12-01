package pl.touk.sputnik.processor.sonar;

import java.util.List;

import pl.touk.sputnik.review.Review;
import pl.touk.sputnik.review.ReviewFile;

import com.google.common.collect.Lists;

class SonarRunnerBuilder {
    public SonarRunner prepareRunner(Review review) {
        List<String> files = Lists.newArrayList();
        for (ReviewFile file : review.getFiles()) {
            System.out.println("111111112");
            System.out.println(file.getSourceDir());
            System.out.println(file.getReviewFilename());
            System.out.println("111111111");

            // files.add("C:\\cygwin\\tmp\\sonar\\publisher\\criteo-rta\\" +  file.getReviewFilename().replace('/', '\\'));
            // files.add(file.getReviewFilename());
            files.add("BaseTester.cs");
            files.add("Actions.cs");
        }
        SonarRunner sonarRunner = new SonarRunner(files);
        return sonarRunner;
    }
}
