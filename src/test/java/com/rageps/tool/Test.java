package com.rageps.tool;

import com.rageps.net.codec.crypto.bcrypt.IpsBCrypt;
import com.rageps.net.codec.login.LoginCode;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.forum.CreateForumAccountTransaction;
import com.rageps.net.sql.forum.SelectForumAccountTransaction;
import com.rageps.net.sql.forum.account.ForumAccount;
import com.rageps.world.entity.actor.player.PlayerCredentials;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class Test {

    public static void main(String[] args) {
        try {




            String password = "test23";
            String username = "testing2";


            /*String salt = IpsBCrypt.gensalt();
            String hash = IpsBCrypt.hashpw(password, salt);

            CreateForumAccountTransaction createForumAccountTransaction = new CreateForumAccountTransaction(username, hash, salt, "127.0.0.1");

            createForumAccountTransaction.onExecute(createForumAccountTransaction.getRepresentation().getWrapper().open());

*/
            PlayerCredentials credentials = new PlayerCredentials(username, password);

            SelectForumAccountTransaction selectForumAccountTransaction = new SelectForumAccountTransaction(credentials);
            ForumAccount forumAccount = selectForumAccountTransaction.onExecute(selectForumAccountTransaction.getRepresentation().getWrapper().open());


            String pw = IpsBCrypt.hashpw(credentials.password, forumAccount.getForumCredentials().getPasswordSalt());
            System.out.println("authenticated:"+(pw.equals(forumAccount.getForumCredentials().getPasswordHash())));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
