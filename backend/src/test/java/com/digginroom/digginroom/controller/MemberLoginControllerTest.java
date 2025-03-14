package com.digginroom.digginroom.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.digginroom.digginroom.controller.dto.MemberLoginRequest;
import com.digginroom.digginroom.domain.Member;
import com.digginroom.digginroom.repository.MemberRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

@SuppressWarnings("NonAsciiCharacters")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberLoginControllerTest extends ControllerTest {

    @Autowired
    private MemberRepository memberRepository;
    private Member member;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        member = new Member("power2", "power1234@");
        memberRepository.save(member);
    }

    @Test
    void 회원가입한_사용자는_로그인을_할_수_있다() {
        Response response = RestAssured.given().log().all()
                .body(new MemberLoginRequest(member.getUsername(), member.getPassword()))
                .contentType(ContentType.JSON)
                .when()
                .post("/login");

        String cookie = response.header("Set-Cookie");
        assertThat(cookie).isNotNull();
    }

    @Test
    void 회원가입을_하지않은_사용자는_로그인을_할_수_없다() {
        RestAssured.given().log().all()
                .body(new MemberLoginRequest("kong123", "kong123!"))
                .contentType(ContentType.JSON)
                .when()
                .post("/login")
                .then().log().all()
                .body("timeStamp", Matchers.notNullValue())
                .body("errorMessage", equalTo("회원 정보가 없습니다."));
    }

    @Test
    void 비밀번호가_틀린_사용자는_로그인을_할_수_없다() {
        RestAssured.given().log().all()
                .body(new MemberLoginRequest(member.getUsername(), "kong123!"))
                .contentType(ContentType.JSON)
                .when()
                .post("/login")
                .then()
                .body("timeStamp", Matchers.notNullValue())
                .body("errorMessage", equalTo("회원 정보가 없습니다."));
    }


    @Test
    void 회원가입한_사용자는_로그인을_연속으로_할_수_있다() {
        RestAssured.given().log().all()
                .body(new MemberLoginRequest(member.getUsername(), member.getPassword()))
                .contentType(ContentType.JSON)
                .when()
                .post("/login");

        Response response = RestAssured.given().log().all()
                .body(new MemberLoginRequest(member.getUsername(), member.getPassword()))
                .contentType(ContentType.JSON)
                .when()
                .post("/login");

        String cookie = response.header("Set-Cookie");
        assertThat(cookie).isNotNull();
    }
}
