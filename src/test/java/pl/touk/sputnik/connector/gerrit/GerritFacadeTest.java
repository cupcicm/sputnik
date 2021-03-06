package pl.touk.sputnik.connector.gerrit;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.touk.sputnik.review.ReviewFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GerritFacadeTest {

    @Mock
    private GerritConnector gerritConnectorMock;

    @InjectMocks
    private GerritFacade gerritFacade;

    @Test
    public void shouldParseListFilesResponse() throws IOException, URISyntaxException {
        String listFilesJson = Resources.toString(Resources.getResource("json/gerrit-listfiles.json"), Charsets.UTF_8);
        when(gerritConnectorMock.listFiles()).thenReturn(listFilesJson);

        List<ReviewFile> reviewFiles = gerritFacade.listFiles();

        assertThat(reviewFiles).isNotEmpty();
    }

    @Test
    public void shouldNotListDeletedFiles() throws IOException, URISyntaxException {
        String listFilesJson = Resources.toString(Resources.getResource("json/gerrit-listfiles.json"), Charsets.UTF_8);
        when(gerritConnectorMock.listFiles()).thenReturn(listFilesJson);

        List<ReviewFile> reviewFiles = gerritFacade.listFiles();

        assertThat(reviewFiles).hasSize(1);
    }

}