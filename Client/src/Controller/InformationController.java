package Controller;

import CommonClass.CommitClass;
import CommonClass.Contributor;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author Moaz
 */
public class InformationController implements Initializable {

    @FXML
    private AreaChart<?, ?> CommitChart;
    @FXML
    private BarChart<?, ?> ContributorsChart;

    private XYChart.Series xy;
    private List<CommitClass> listCommit = null;
    private List<Contributor> listContributor = null;
    private long TimeStep = 1000 * 60 * 60 * 24; ///Day
    private SimpleDateFormat ft = new SimpleDateFormat("MM/dd");

    public InformationController(List<CommitClass> listCommit, List<Contributor> listContributor) {
        this.listCommit = listCommit;
        this.listContributor = listContributor;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        drawCommitChart();
        drawContributorsChart();
    }

    private void drawCommitChart() {
        xy = new XYChart.Series<>();
        //      xy.setName("Name");
        if (listCommit.size() == 0) {
            return;
        }
        Date MaxDate = listCommit.get(0).MyDate, MinDate = listCommit.get(0).MyDate;
        for (CommitClass c : listCommit) {
            if (c.MyDate.after(MaxDate)) {
                MaxDate = c.MyDate;
            }
            if (c.MyDate.before(MinDate)) {
                MinDate = c.MyDate;
            }
        }
        while (MinDate.before(MaxDate)) {
            Date newDate = new Date(MinDate.getTime() + TimeStep);
            int num = 0;
            for (CommitClass c : listCommit) {
                Date d = c.MyDate;
                if ((d.after(MinDate) || d.equals(MinDate)) && d.before(newDate)) {
                    num++;
                }
            }
            xy.getData().add(new XYChart.Data<>(ft.format(MinDate), num));
            MinDate = newDate;
        }
        CommitChart.getData().add(xy);
    }

    private void drawContributorsChart() {
        xy = new XYChart.Series<>();
        //      xy.setName("Name");
        for (Contributor c : listContributor) {
            int numCommit = 0;
            for (CommitClass commit : listCommit) {
                if (commit.Author.equals(c.Name)) {
                    numCommit++;
                }
            }
            xy.getData().add(new XYChart.Data<>(c.Name, numCommit));
        }

        ContributorsChart.getData().add(xy);
    }
}
