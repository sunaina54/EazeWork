package hr.eazework.Notification;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by dell1 on 01-11-2017.
 */
public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here

        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }
}
