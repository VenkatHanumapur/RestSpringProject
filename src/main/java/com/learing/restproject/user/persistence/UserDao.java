package com.learing.restproject.user.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Repository;

import com.learing.restproject.user.User;

@Repository
public class UserDao {
	
	private static List<User> users = new ArrayList<>();
	
	private static int countid=0;
	
	static {
		
		users.add(new User(++countid,"James",LocalDate.now().minusYears(25)));
		users.add(new User(++countid,"Lisa",LocalDate.now().minusYears(23)));
		users.add(new User(++countid,"Lory",LocalDate.now().minusYears(45)));
		users.add(new User(++countid,"Jacky",LocalDate.now().minusYears(38)));
		
	}
	
	
	public List<User> findAll() {
		return users;
	}
	
	public User save(User user) {
		user.setId(++countid);
		users.add(user);
		return user;
	}
	
	public User findById(int id) {
		Predicate<? super User> predicate = e -> e.getId()==id;
		return users.stream().filter(predicate).findFirst().orElse(null);
	}
	
	public void deleteById(int id) {
		User user = users.stream().filter(e -> e.getId()==id).findFirst().get();
		users.remove(user);
	}

}
