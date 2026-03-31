package com.deep.moneymanager.service;


import com.deep.moneymanager.dto.ExpenceDTO;
import com.deep.moneymanager.entity.ProfileEntity;
import com.deep.moneymanager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private  final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;



    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

    @Scheduled(cron = "0 0 22 * * *",zone = "IST")
    public void sendDailyIncomeExpenseReminder(){
        log.info("job started:sendDailyIncomeExpenseReminder()");
        List<ProfileEntity>profileEntities =profileRepository.findAll();
        for(ProfileEntity profile:profileEntities){
            String body="Hi " +profile.getFullName()+",<br><br>"
                    +"this is a friendly reminder to add income and expenses for today in Money Manger .<br><br>"
                    +"<a href"+frontendUrl+"  style='display:inline-block;padding10px 20pz;background'"
                    +"<br><br> Best regards,<br>Money Manager Team";

            emailService.sendEmail(profile.getEmail(),"Daily reminder:add your Inacome and expeses", body);
        }
    }

    @Scheduled(cron = "0 0 23 * * *",zone = "IST")
    public  void sendDailyExpenseSummary(){
        log.info("Job started:sendDailyExpenseSummary()");
        List<ProfileEntity>profileEntities=profileRepository.findAll();
       //List<ExpenceDTO>todayExpenses= expenseService.getExpensesForUserOnDate(profileEntities.getId(),LocalDate.now());
        for (ProfileEntity profile : profileEntities) {

            List<ExpenceDTO> ll = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());

            if (!ll.isEmpty()) {

                StringBuilder table = new StringBuilder();

                table.append("<table style='border-collapse:collapse;width:100%'>");
                table.append("<tr style='background-color:#f2f2f2;'>");
                table.append("<th>#</th><th>Name</th><th>Amount</th><th>Category</th>");
                table.append("</tr>");

                int i = 1;
                for (ExpenceDTO exp : ll) {
                    table.append("<tr>");
                    table.append("<td>").append(i++).append("</td>");
                    table.append("<td>").append(exp.getName()).append("</td>");
                    table.append("<td>").append(exp.getAmount()).append("</td>");
                    table.append("<td>").append(exp.getCategoryName()).append("</td>");
                    table.append("</tr>");
                }

                table.append("</table>");

                String body = "Hi " + profile.getFullName()
                        + ",<br><br>Here is your today's expense summary:<br><br>"
                        + table
                        + "<br><br>Best regards,<br>Money Manager Team";

                emailService.sendEmail(profile.getEmail(), "Your Daily Expense Summary", body);
            }
        }
        log.info("Job completed:sendDailyExpenseSummary()");
       }
    }

