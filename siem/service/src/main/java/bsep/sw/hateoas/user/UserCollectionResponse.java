package bsep.sw.hateoas.user;

import bsep.sw.domain.User;
import bsep.sw.hateoas.PaginationLinks;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserCollectionResponse {
    public List<UserResponseData> data;
    public PaginationLinks links;

    public UserCollectionResponse(final List<UserResponseData> data, final PaginationLinks links) {
        this.data = data;
        this.links = links;
    }

    public static UserCollectionResponse fromDomain(final Set<User> users, final PaginationLinks links) {
        return new UserCollectionResponse(users.stream().map(UserResponseData::fromDomain).collect(Collectors.toList()), links);
    }
}
