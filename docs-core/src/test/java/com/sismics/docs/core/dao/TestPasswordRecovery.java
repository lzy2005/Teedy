package com.sismics.docs.core.dao.jpa;

import com.sismics.docs.BaseTransactionalTest;
import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.dao.PasswordRecoveryDao;
import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.core.model.jpa.PasswordRecovery;
import com.sismics.docs.core.util.TransactionUtil;
import com.sismics.docs.core.util.authentication.InternalAuthenticationHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the persistance layer.
 * 
 * @author jtremeaux
 */
public class TestPasswordRecovery extends BaseTransactionalTest {
    @Test
    public void testPasswordRecovery() throws Exception {
        // Create a user
        UserDao userDao = new UserDao();
        User user = createUser("testPasswordRecovery");
        TransactionUtil.commit();

        // Test Create
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setUsername(user.getUsername());

        PasswordRecoveryDao passwordRecoveryDao = new PasswordRecoveryDao();
        String id = passwordRecoveryDao.create(passwordRecovery);

        Assert.assertNotNull(id);
        Assert.assertEquals(id, passwordRecovery.getId());
        Assert.assertNotNull(passwordRecovery.getCreateDate());

        // Delete the created user
        userDao.delete("testJpa", user.getId());
        TransactionUtil.commit();
    }
}
