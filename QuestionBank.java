package com.example.simpleapiquiz.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.simpleapiquiz.controller.AppController;
import com.example.simpleapiquiz.model.Questions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.simpleapiquiz.controller.AppController.getInstance;

public class QuestionBank {

    ArrayList<Questions> questionsArrayList=new ArrayList<Questions>();

    private String url="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public  List<Questions> getQuestions(final AnswerListAsyncResponse callBack){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,
                url,(JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("item","data"+response.get(0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i=0;i<response.length();i++)
                        {

                            try {
                                Questions questions=new Questions();
                                questions.setAnswer(response.getJSONArray(i).get(0).toString());
                                questions.setAnswerTrue(response.getJSONArray(i).getBoolean(1));

                                questionsArrayList.add(questions);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (null != callBack) callBack.processFinished(questionsArrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionsArrayList;

    }
}
