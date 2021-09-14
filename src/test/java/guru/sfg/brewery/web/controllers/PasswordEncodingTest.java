package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;

public class PasswordEncodingTest {
    static final String PASSWORD = "password";


    @Test
    void testBcrypt(){
        PasswordEncoder bCrypt = new BCryptPasswordEncoder(10);
        System.out.println(bCrypt.encode("oliveira"));
        System.out.println(bCrypt.encode(PASSWORD));

       /** bCrypt = new BCryptPasswordEncoder(16);
        System.out.println(bCrypt.encode(PASSWORD));
        System.out.println(bCrypt.encode(PASSWORD));*/
    }

    @Test
    void testSha256(){
        PasswordEncoder sha256 = new StandardPasswordEncoder();
        System.out.println(sha256.encode("oliveira"));
        System.out.println(sha256.encode(PASSWORD));
    }

    @Test
    void testLdap(){
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        System.out.println(ldap.encode("oliveira"));
        System.out.println(ldap.encode(PASSWORD));

        String encodedPwd = ldap.encode(PASSWORD);

        assertTrue(ldap.matches(PASSWORD,encodedPwd));

    }

    @Test
    void TestNoOp(){
        PasswordEncoder noOp= NoOpPasswordEncoder.getInstance();
        System.out.println(noOp.encode(PASSWORD));
    }

    @Test
    void hasingExample(){
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        String salted =  PASSWORD + "adicionando valores ao password";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
    }
}
