package pan.alexander.notes.domain;


import android.content.Intent;

import javax.inject.Inject;

import io.reactivex.Completable;
import pan.alexander.notes.data.database.NotesDao;
import pan.alexander.notes.domain.account.User;

public class AccountInteractor {
    private final AccountRepository accountRepository;

    @Inject
    public AccountInteractor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public User getUser() {
        return accountRepository.getUser();
    }

    public Intent signIn(boolean allowAnonymousUsersAutoUpgrade) {
        return accountRepository.signIn(allowAnonymousUsersAutoUpgrade);
    }

    public Completable signInAnonymously() {
        return accountRepository.signInAnonymously();
    }

    public Completable signOut() {
        return accountRepository.signOut();
    }

    public void setUser(User user) {
        accountRepository.setUser(user);
    }

    public Completable deleteCurrentUser() {
        return accountRepository.deleteCurrentUser();
    }
}
