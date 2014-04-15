/*************************GO-LICENSE-START*********************************
 * Copyright 2014 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************GO-LICENSE-END***********************************/

package com.thoughtworks.go.server.security;

import java.util.List;
import java.util.ArrayList;

import com.thoughtworks.go.presentation.UserSearchModel;
import com.thoughtworks.go.presentation.UserSourceType;
import com.thoughtworks.go.domain.User;
import com.thoughtworks.go.server.service.result.HttpLocalizedOperationResult;
import com.thoughtworks.go.server.service.GoConfigService;
import com.thoughtworks.go.i18n.LocalizedMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @understands searching for users(from authentication sources)
 */
@Service
public class UserSearchService {
    private final LdapUserSearch ldapUserSearch;
    private final PasswordFileUserSearch passwordFileUserSearch;
    private GoConfigService goConfigService;

    private static final Logger LOGGER = Logger.getLogger(UserSearchService.class);
    private static final int MINIMUM_SEARCH_STRING_LENGTH = 2;

    @Autowired
    public UserSearchService(LdapUserSearch ldapUserSearch, PasswordFileUserSearch passwordFileUserSearch, GoConfigService goConfigService) {
        this.ldapUserSearch = ldapUserSearch;
        this.passwordFileUserSearch = passwordFileUserSearch;
        this.goConfigService = goConfigService;
    }

    public List<UserSearchModel> search(String searchText, HttpLocalizedOperationResult result) {
        List<UserSearchModel> userSearchModels = new ArrayList<UserSearchModel>();
        if (isInputValid(searchText, result)) {
            return userSearchModels;
        }
        boolean passwordSearchFailed = searchPasswordFile(searchText, result, userSearchModels);
        searchLdap(searchText, result, userSearchModels, passwordSearchFailed);

        if (userSearchModels.size() == 0 && !result.hasMessage()) {
           result.setMessage(LocalizedMessage.string("NO_SEARCH_RESULTS_ERROR"));
        }
        
        return userSearchModels;
    }

    private void searchLdap(String searchText, HttpLocalizedOperationResult result, List<UserSearchModel> userSearchModels, boolean passwordSearchFailed) {
        if (goConfigService.isLdapConfigured()) {
            List<User> users = new ArrayList<User>();
            try {
                users = ldapUserSearch.search(searchText);
            } catch (LdapUserSearch.NotAllResultsShownException ex) {
                result.setMessage(LocalizedMessage.string("NOT_ALL_RESULTS_SHOWN"));
                users = ex.getUsers();
            } catch (Exception ex) {
                LOGGER.error(String.format("User search for %s on ldap failed with Exception.", searchText), ex);
                if (passwordSearchFailed) {
                    result.badRequest(LocalizedMessage.string("USER_SEARCH_FAILED"));
                } else {
                    result.setMessage(LocalizedMessage.string("LDAP_ERROR"));
                }
            }
            userSearchModels.addAll(convertUsersToUserSearchModel(users, UserSourceType.LDAP));
        }
    }

    private boolean searchPasswordFile(String searchText, HttpLocalizedOperationResult result, List<UserSearchModel> userSearchModels) {
        boolean passwordSearchFailed = false;
        if(!goConfigService.isPasswordFileConfigured()){
            return false;
        }
        try {
            List<User> passwordFileUsers = passwordFileUserSearch.search(searchText);
            List<UserSearchModel> models = convertUsersToUserSearchModel(passwordFileUsers, UserSourceType.PASSWORD_FILE);
            userSearchModels.addAll(models);
        } catch (Exception e) {
            passwordSearchFailed = true;
            result.setMessage(LocalizedMessage.string("PASSWORD_SEARCH_FAILED"));
            LOGGER.error(String.format("User search for %s on password failed with IOException.", searchText),e);
        }
        return passwordSearchFailed;
    }

    private boolean isInputValid(String searchText, HttpLocalizedOperationResult result) {
        if (searchText.trim().length() < MINIMUM_SEARCH_STRING_LENGTH) {
            result.badRequest(LocalizedMessage.string("SEARCH_STRING_TOO_SMALL"));
            return true;
        }
        return false;
    }

    private List<UserSearchModel> convertUsersToUserSearchModel(List<User> users, UserSourceType source) {
        List<UserSearchModel> userSearchModels = new ArrayList<UserSearchModel>();
        for (User user : users) {
            userSearchModels.add(new UserSearchModel(user, source));
        }
        return userSearchModels;
    }

}
