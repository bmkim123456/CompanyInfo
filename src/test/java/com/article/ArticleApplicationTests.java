package com.article;

import com.article.service.CompanySendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class ArticleApplicationTests {

	@Autowired
	CompanySendService companySendService;

	@Test
	void contextLoads() {
	}


	@Test
	void makeCircle () {

		int firstLine = 7;
		int secondLine = 9;

		String firstLineText = "";
		String secondLineText = "";

		for (int i = 0; i < firstLine; i++) {
			if (i == 2 || i == 6) {
				firstLineText += "*";
			} else 	firstLineText += " ";
		}

		for (int i = 0; i < secondLine; i++) {
			if (i == 0 || i == 8) {
				secondLineText += "*";
			} else 	secondLineText += " ";
		}

		System.out.println(firstLineText);
		System.out.println(secondLineText);
		System.out.println(firstLineText);

	}

}
