package pl.touk.sputnik.engine.visitor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.review.Comment;
import pl.touk.sputnik.review.Review;
import pl.touk.sputnik.review.ReviewFile;

import java.util.Iterator;

@Slf4j
@AllArgsConstructor
public class LimitCommentVisitor implements AfterReviewVisitor {
    private static final String MESSAGE_FORMAT = "Showing only first %d comments. %d comments are filtered out";
    private int maximumCount;

    @Override
    public void afterReview(@NotNull Review review) {
        if (review.getTotalViolationsCount() <= maximumCount) {
            log.info("There are {} total violations for this review, which is below maximum comment count {}. No comments are filtered", review.getTotalViolationsCount(), maximumCount);
            return;
        }
        log.info("There are {} total violations for this review, which is higher than maximum comment count {}. {} comments will be filtered out.", review.getTotalViolationsCount(), maximumCount, review.getTotalViolationsCount() - maximumCount);
        filterOutComments(review);
        addMessage(review);
    }

    private void filterOutComments(Review review) {
        int counter = 0;
        for (ReviewFile reviewFile : review.getFiles()) {
            Iterator<Comment> iterator = reviewFile.getComments().iterator();
            while(iterator.hasNext()) {
                iterator.next();
                counter++;
                if (counter > maximumCount) {
                    iterator.remove();
                }
            }
        }
    }

    private void addMessage(Review review) {
        review.getMessages().add(String.format(MESSAGE_FORMAT, maximumCount, review.getTotalViolationsCount() - maximumCount));
    }
}
