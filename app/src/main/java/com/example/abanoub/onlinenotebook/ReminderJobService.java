package com.example.abanoub.onlinenotebook;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
/**
 * Created by Abanoub on 2017-07-06.
 */

public class ReminderJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here


        return false; // Answers the question: "Is there still work going on?"    }

    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"

    }
}
