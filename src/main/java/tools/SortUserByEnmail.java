package tools;

import dto.UserDto;

import java.util.Comparator;

public class SortUserByEnmail implements Comparator<UserDto> {
    public int compare(UserDto a, UserDto b) { return a.getEmail().compareTo(b.getEmail());  }
}

