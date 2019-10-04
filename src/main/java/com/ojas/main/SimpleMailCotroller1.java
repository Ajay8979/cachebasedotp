package com.ojas.main;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@RestController
@RequestMapping("/sendingmail")
public class SimpleMailCotroller1 {

//	static Logger log = Logger.getLogger(SimpleMailCotroller1.class);
	Logger logger = Logger.getLogger(SimpleMailCotroller1.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@GetMapping("/mail")
	public String sendMail() {

		/*
		 * long min = 10; long max = 99; long random = (int) (Math.floor(Math.random() *
		 * (max - min + 1)) + min); long min1 = 10; long max2 = 99; long random2 = (int)
		 * (Math.floor(Math.random() * (max2 - min1 + 1)) + min1); String s11 = ("" +
		 * random); String s22 = ("" + random2); String otp = s11 + s22; //
		 * System.out.println(otp); logger.info(" this is secret otp:" + otp);
		 */

		System.out.println("dfhhsd");
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		Integer otp1 = generateOTP("ajay.r185487@gmail.com");

		simpleMailMessage.setTo("ajay.r185487@gmail.com");
		simpleMailMessage.setSubject("nothing");
		simpleMailMessage.setText("the secret otp key is:" + otp1.toString());

		javaMailSender.send(simpleMailMessage);
		return "sended successfully..";

	}

	// cache based otp generation

	private static final Integer EXPIRE_MIN = 5;
	private static LoadingCache<String, Integer> otpCache;

	public SimpleMailCotroller1() {
		super();
		otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Integer>() {
					@Override
					public Integer load(String s) throws Exception {
						return 0;
					}
				});
	}

	public static Integer generateOTP(String key) {
		Random random = new Random();
		int OTP = 1000 + random.nextInt(9000);
		otpCache.put(key, OTP);

		return OTP;
	}

	@GetMapping("/verifyotp")
	public Integer getOPTByKey(String key) {
		logger.info("helloooo");
		try {
			return otpCache.get("8973");
		} catch (ExecutionException e) {
			return -1;
		}
	}

	public void clearOTPFromCache(String key) {
		otpCache.invalidate(key);
	}

}
