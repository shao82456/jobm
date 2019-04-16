package job;

import lombok.extern.log4j.Log4j;
import org.quartz.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Log4j
public  class CommandJob implements Job {
    /**
     * Quartz requires a public empty constructor so that the
     * scheduler can instantiate the class whenever it needs.
     */
    public CommandJob() { }

    /**
     * <p>
     * Called by the <code>{@link org.quartz.Scheduler}</code> when a
     * <code>{@link org.quartz.Trigger}</code> fires that is associated with
     * the <code>Job</code>.
     * </p>
     *
     * @throws JobExecutionException
     *             if there is an exception while executing the job.
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap=jobExecutionContext.getJobDetail().getJobDataMap();
        String name=dataMap.getString("name");
        String command=dataMap.getString("command");
        String logfile=dataMap.getString("logfile");
        String[] cmd={"bash -c",command,">"+logfile+"2 >&1"};

        try {
            log.info(jobExecutionContext.getScheduler().getSchedulerInstanceId()+" start run this job");
            Process process=null;
            process=Runtime.getRuntime().exec(cmd);
            if(process==null) {
                log.error("系统进程获取失败!");
                return;
            }
        } catch (Exception e) {
            log.error("job:"+name+"执行失败 command="+command);
            log.error(e.getMessage());
        }
    }
}
