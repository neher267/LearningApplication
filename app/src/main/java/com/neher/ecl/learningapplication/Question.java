package com.neher.ecl.learningapplication;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/25/2018.
 */

public class Question {
    private String question;
    private String subject;
    private String option_1;
    private String option_2;
    private String option_3;
    private String option_4;
    private String ans;
    private int weight;

    public Question(String question, String subject, String option_1, String option_2, String option_3, String option_4, String ans, int weight){
        this.question = question;
        this.subject = subject;
        this.option_1 = option_1;
        this.option_2 = option_2;
        this.option_3 = option_3;
        this.option_4 = option_4;
        this.ans = ans;
        this.weight = weight;
    }

    public String getQuestion(){
        return question;
    }
    public String getSubject(){
        return subject;
    }
    public String getOption_1(){
        return option_1;
    }
    public String getOption_2(){
        return option_2;
    }
    public String getOption_3(){
        return option_3;
    }
    public String getOption_4(){
        return option_4;
    }
    public String getAns(){
        return ans;
    }
    public int getWeight(){
        return weight;
    }

    public static ArrayList<Question> getQuestions(){
        ArrayList<Question> questions = new ArrayList<>();

        Question question1 = new Question("Q. 5 * 10 = ?", "math", "50", "30", "75","20", "1", 3);
        questions.add(question1);

        Question question2 = new Question("Q. 6 * 10 = ?", "math","50", "30", "60","20", "3", 3);
        questions.add(question2);

        return questions;

    }
}
