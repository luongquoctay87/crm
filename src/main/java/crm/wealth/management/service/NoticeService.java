package crm.wealth.management.service;

import crm.wealth.management.model.AppUser;
import crm.wealth.management.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class NoticeService {

    private final UserRepo userRepo;
    private final MailService mailService;

    @Scheduled(cron = "0 0 0 * * ?")
    private void sendMailNotice() {
        try {
            log.info("Start send mall to notice task's user");
            List<AppUser> appUsers = (List<AppUser>) userRepo.findAll();
            // List email
            List<String> emails = appUsers.stream().map(AppUser::getEmail).collect(Collectors.toList());
            // list task by email
//            List<String> tasks = ....
//            Map<String, String> mapTask = new HashMap<>();
//            for (String mail : emails) {
//                mapTask.put(mail, "1. ...., 2. .....");
//            }
            // send mail
            for (String mail : emails) {
                mailService.sendSimpleMessage(mail, "[TASK] - Remaining task open", "1/ ..... 2/ .....");
            }
            log.info("End send mail to notice's task successfully");
        } catch (Exception e) {
            log.error("Send mall to notice error --->");
            log.error(e.getMessage());
        }
    }
}
