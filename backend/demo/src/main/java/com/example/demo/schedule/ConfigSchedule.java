package com.example.demo.schedule;

import com.example.demo.entity.ForgotPasswordEntity;
import com.example.demo.repository.ForgotPasswordRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
public class ConfigSchedule {

    private final ForgotPasswordRepository forgotPasswordRepository;

    public ConfigSchedule(ForgotPasswordRepository forgotPasswordRepository) {
        this.forgotPasswordRepository = forgotPasswordRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(fixedRate = 10000)
    public void performScan() {
        List<ForgotPasswordEntity> forgotPasswordEntityList = forgotPasswordRepository.getListByExpiredTime();
        forgotPasswordRepository.deleteAll(forgotPasswordEntityList);
    }
}
