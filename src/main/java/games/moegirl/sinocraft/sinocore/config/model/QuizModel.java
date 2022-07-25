package games.moegirl.sinocraft.sinocore.config.model;

import cn.hutool.cron.CronUtil;
import com.google.gson.Gson;
import games.moegirl.sinocraft.sinocore.SinoCore;
import games.moegirl.sinocraft.sinocore.config.QuizQuestionsConfig;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class QuizModel {
    private static final Gson GSON = new Gson();
    public Calendar lastUpdated = Calendar.getInstance();

    public String date = "";

    public List<Question> questions = new ArrayList<>();

    public static class Question {
        public String question;
        public List<Answer> answers;

        public String question() {
            return question;
        }

        public List<Answer> answers() {
            return answers;
        }

        public static class Answer {
            public String answer;
            public String mark;
            public boolean isCorrect;

            public String answer() {
                return answer;
            }

            public boolean isCorrect() {
                return isCorrect;
            }
        }
    }

    public static QuizModel fetch() {
        try {
            var dataUrl = QuizQuestionsConfig.DATA_URL;
            var url = new URL(dataUrl.get());

            var data = IOUtils.toString(url.toURI(), StandardCharsets.UTF_8);

            var ret = GSON.fromJson(data, QuizModel.class);
            ret.reFetch();
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        var q = new QuizModel();
        q.reFetch();
        return q;
    }

    public Question random() {
        var rand = new Random().nextInt(0, questions.size());
        return questions.get(rand);
    }

    public void reFetch() {
        CronUtil.schedule("* 2 * * *", (Runnable) this::doReFetch);
    }

    public void doReFetch() {
        SinoCore.getLogger().info("Fetching quiz data.");

        try {
            var dataUrl = QuizQuestionsConfig.DATA_URL;
            var url = new URL(dataUrl.get());

            var data = IOUtils.toString(url.toURI(), StandardCharsets.UTF_8);

            var model = GSON.fromJson(data, QuizModel.class);

            lastUpdated = Calendar.getInstance();
            date = model.date;
            questions = model.questions;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
