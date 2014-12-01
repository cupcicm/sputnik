package pl.touk.sputnik.processor.sonar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import lombok.AllArgsConstructor;

import org.apache.commons.lang.StringUtils;
import org.sonar.runner.api.EmbeddedRunner;


@AllArgsConstructor
public class SonarRunner {

    private final List<String> files;

    public void run() {
        runInProcess();
    }

    private void runInProcess() {
        EmbeddedRunner runner = EmbeddedRunner.create();
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("C:\\tools\\sonar-runner\\conf\\sonar-runner.properties"));
            prop.load(new FileInputStream("sonar-project.properties"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //prop.put("sonar.inclusions", "" + StringUtils.join(files, ","));
        prop.put("sonar.analysis.mode", "preview");
        prop.put("sonar.scm.enabled", "false");
        prop.put("sonar.scm-stats.enabled", "false");
        //prop.put("sonar.issuesReport.console.enable", "true");
        prop.put("issueassignplugin.enabled", "true");
        runner.addProperties(prop);
        runner.execute();
    }

    public void runWithSeparateProcess() {
        String sources = StringUtils.join(files, ",");
        Process process;
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "C:\\tools\\sonar-runner\\bin\\sonar-runner.bat", "-Dsonar.analysis.mode=incremental", "-Dsonar.scm.enabled=false",
                    "-Dsonar.scm-stats.enabled=false", "-Dissueassignplugin.enabeld=false");
            builder.directory(new File(".").getAbsoluteFile());
            System.out.println(builder.directory());
            System.out.println(                    "C:\\tools\\sonar-runner\\bin\\sonar-runner.bat -Dsonar.analysis.mode=incremental -Dsonar.scm.enabled=false -Dsonar.scm-stats.enabled=false -Dissueassignplugin.enabeld=false -Dsonar.sources=" + sources);
            process = builder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();

            InputStream err = process.getErrorStream();
            InputStreamReader errr = new InputStreamReader(err);
            BufferedReader berr = new BufferedReader(errr);
            while (line != null) {
                //System.out.println(line);
                line = br.readLine();
            }
            String errLine = berr.readLine();
            while (errLine != null) {
                System.err.println(errLine);
                errLine = berr.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
