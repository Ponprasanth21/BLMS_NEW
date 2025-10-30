package com.bornfire.config;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
//import java.util.Date;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.entities.Access_Role_Entity;
import com.bornfire.entities.Access_Role_Repo;
import com.bornfire.entities.BGLSAuditTable;
import com.bornfire.entities.BGLSAuditTable_Rep;
import com.bornfire.entities.BGLS_CONTROL_TABLE_REP;
import com.bornfire.entities.BGLS_Control_Table;
import com.bornfire.entities.UserProfile;
import com.bornfire.entities.UserProfileRep;
import com.bornfire.services.AuditConfigure;
import com.bornfire.services.LoginServices;

@Configuration
@EnableWebSecurity
public class BGLSWebSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	UserProfileRep userProfileRep;

	@Autowired
	Access_Role_Repo access_Role_Repo;
	
	@Autowired
	BGLSAuditTable_Rep bGLSAuditTable_Rep;

	@Autowired
	SequenceGenerator sequence;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	LoginServices loginServices;

	@Autowired
	PasswordEncryption passwordEncryption;

	@Autowired
	Environment env;

	@Autowired
	BGLS_CONTROL_TABLE_REP bGLS_CONTROL_TABLE_REP;
	
	@Autowired
	AuditConfigure audit;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/css/**","/js/**",  "/webfonts/**",  "/images/**", "/login*", "/freezeColumn/**","favicon.ico")
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll()
				.failureHandler(blrsAuthFailHandle()).successHandler(blrsAuthSuccessHandle())
				.usernameParameter("userid").and().logout().permitAll().and().logout()
				.logoutSuccessHandler(xbrlLogoutSuccessHandler()).permitAll().and().sessionManagement()
				.maximumSessions(3).maxSessionsPreventsLogin(false);
		http.csrf().disable();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean

	public AuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider ap = new DaoAuthenticationProvider() {

			@Override
			@Transactional
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				String userid = authentication.getName();
				String password = authentication.getCredentials().toString();
				Optional<UserProfile> up = userProfileRep.findById(userid);

				try {
					if (up.isPresent() && up.get().getDel_flg().equals("N")) {
						UserProfile usr = up.get();

						if (!usr.isAccountNonExpired()) {

							throw new AccountExpiredException("Account Expired");

						}else if (!usr.isCredentialsNonExpired()) {

							throw new CredentialsExpiredException("Credentials Expired");

						} else if (!usr.isAccountNonLocked()) {

							throw new LockedException("Account Locked");

						} else if (!usr.isEnabled()) {

							throw new DisabledException("Account Disabled");

						} else if(!usr.isVerified()) {
							throw new DisabledException("Account Need to Verify");
						}
						/*
						 * else if (!usr.isLoginAllowed()) {
						 * 
						 * throw new LockedException("Login Not Allowed");
						 * 
						 * }
						 */ 

						if (!PasswordEncryption.validatePassword(password, usr.getPassword())) {

							logger.info("Passing Userid :" + userid);

							Session hs = sessionFactory.getCurrentSession();
							Transaction tr = hs.getTransaction();
							hs.createQuery(
									"update UserProfile a set a.no_of_attmp=nvl(a.no_of_attmp,0)+1, a.user_locked_flg=decode(nvl(a.no_of_attmp,0)+1,'3','Y','N'), a.login_status=decode(nvl(a.no_of_attmp,0)+1,'3','Inactive','Active') where userid=?1")
									.setParameter(1, userid).executeUpdate();
							tr.commit();
							hs.close();
							throw new BadCredentialsException("Authentication failed");

						} else {

							return new UsernamePasswordAuthenticationToken(userid, password, Collections.emptyList());

						}

					} else {

						throw new UsernameNotFoundException("Invalid User Name");
					}

				} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
					e.printStackTrace();
					logger.info(" Userid :" + userid);
					authentication.setAuthenticated(false);
				}
				return authentication;

			}

			@Override
			public boolean supports(Class<?> aClass) {
				return aClass.equals(UsernamePasswordAuthenticationToken.class);
			}

		};

		ap.setHideUserNotFoundExceptions(false);
		ap.setUserDetailsService(userDetailsService());

		return ap;

	}

	@Bean
	public AuthenticationFailureHandler blrsAuthFailHandle() {
		return new AuthenticationFailureHandler() {

			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {

				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				// logger.info(exception.getMessage());
				response.sendRedirect("login?error=" + exception.getMessage());

			}

		};

	}

	@Bean
	public AuthenticationSuccessHandler blrsAuthSuccessHandle() {
		return new AuthenticationSuccessHandler() {

			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				String auditID = sequence.generateRequestUUId();
				
				Optional<UserProfile> up = userProfileRep.findById(authentication.getName());
				
				BGLS_Control_Table up1 = bGLS_CONTROL_TABLE_REP.getTranDate();
				
				
				
				UserProfile user = up.get();
				Access_Role_Entity access_role =  access_Role_Repo.getRole(user.getUserid());
				/*
				 * user.setNo_of_attmp(0); user.setUser_locked_flg("N");
				 */

				// user.setLocked_flg("N");
				userProfileRep.save(user);

				/*
				 * loginServices.SessionLogging("LOGIN", "M1", request.getSession().getId(),
				 * user.getUserid(), request.getRemoteAddr(), "ACTIVE");
				 */
				request.getSession().setAttribute("TRANDATE", up1.getTran_date());
				request.getSession().setAttribute("USERID", user.getUserid());

				request.getSession().setAttribute("USERNAME", user.getUsername());
				request.getSession().setAttribute("BRANCH_ID", user.getBranch_id());
				request.getSession().setAttribute("BRANCH_DESC", user.getBranch_des());
				request.getSession().setAttribute("ROLEID", "");
				/* request.getSession().setAttribute("PERMISSIONS", user.getPermissions()); */
				/* request.getSession().setAttribute("WORKCLASS", user.getWork_class()); */
				request.getSession().setAttribute("PASSWORD", user.getPassword());
				// request.getSession().setAttribute("birthday", user.getDob());
				request.getSession().setAttribute("LOGIN_TIME",
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

				// ------------    For Screen Accesss  ----------------------
				
				request.getSession().setAttribute("SCREEN_ACCESS_ADMIN", (access_role != null && access_role.getAdmin() != null ) ? access_role.getAdmin() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_ORGNAIZATION_DETAILS", (access_role != null && access_role.getOrgnaization_details() != null ) ? access_role.getOrgnaization_details() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_USER_CONTROLS", (access_role != null && access_role.getUser_controls() != null ) ? access_role.getUser_controls() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_REFERENCE_CODE_MAINTENANCE", (access_role != null && access_role.getReference_code_maintenance() != null ) ? access_role.getReference_code_maintenance() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_AUDIT_TRAIL", (access_role != null && access_role.getAudit_trail() != null ) ? access_role.getAudit_trail() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_DAY_END_OPERATION_USER", (access_role != null && access_role.getDay_end_operation() != null) ? access_role.getDay_end_operation() : "N");
				request.getSession().setAttribute("SCREEN_ACCESS_CUSTOMER_MAINTENANCE", (access_role != null && access_role.getCustomer_maintenance() != null) ? access_role.getCustomer_maintenance() : "N");
				request.getSession().setAttribute("SCREEN_ACCESS_LOAN_MAINTENANCE", (access_role != null && access_role.getLoan_maintanace() != null) ? access_role.getLoan_maintanace() : "N");
				request.getSession().setAttribute("SCREEN_ACCESS_MIGRATION", (access_role != null && access_role.getMigration() != null ) ? access_role.getMigration() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_CUSTOMER_MASTER", (access_role != null && access_role.getCustomer_master() != null ) ? access_role.getCustomer_master() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_LOAN_MASTER", (access_role != null && access_role.getLoan_master() != null ) ? access_role.getLoan_master() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_LOAN_SCHEDULE_MIGRATION", (access_role != null && access_role.getLoan_schedule_migration() != null ) ? access_role.getLoan_schedule_migration() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_TRANSACTION_MIGRATION", (access_role != null && access_role.getTransaction_migration() != null ) ? access_role.getTransaction_migration() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_LOAN_OPERATION", (access_role != null && access_role.getLoan_operation() != null ) ? access_role.getLoan_operation() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_LOAN_OPERATION_LS", (access_role != null && access_role.getLoan_operation_ls() != null ) ? access_role.getLoan_operation_ls() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_LOAN_CLOSURE", (access_role != null && access_role.getLoan_closure() != null ) ? access_role.getLoan_closure() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_TRANSACTION_MAINTENANCE", (access_role != null && access_role.getTransaction_maintenance() != null ) ? access_role.getTransaction_maintenance() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_JOURNAL_ENTRIES", (access_role != null && access_role.getJournal_entries() != null ) ? access_role.getJournal_entries() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_ACCOUNT_LEDGER_POSTING", (access_role != null && access_role.getAccount_ledger_posting() != null ) ? access_role.getAccount_ledger_posting() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_ACCOUNT_LEDGER", (access_role != null && access_role.getAccount_ledger() != null ) ? access_role.getAccount_ledger() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_TRIAL_BALANCE_T", (access_role != null && access_role.getTrial_balance_t() != null ) ? access_role.getTrial_balance_t() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_PROFIT_AND_LOSS_ACCOUNT_T", (access_role != null && access_role.getProfit_and_loss_account_t() != null ) ? access_role.getProfit_and_loss_account_t() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_COLLECTION_PROCESS", (access_role != null && access_role.getCollection_process() != null ) ? access_role.getCollection_process() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_PARTICIPATING_BANKS", (access_role != null && access_role.getParticipating_banks() != null ) ? access_role.getParticipating_banks() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_LOAN_COLLECTING", (access_role != null && access_role.getLoan_collecting() != null ) ? access_role.getLoan_collecting() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_BATCH_JOB_EXECUTION", (access_role != null && access_role.getBatch_job_execution() != null ) ? access_role.getBatch_job_execution() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_BATCH_JOB", (access_role != null && access_role.getBatch_job() != null ) ? access_role.getBatch_job() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_INQUIRIES_AND_REPORTS", (access_role != null && access_role.getInquiries_and_reports() != null ) ? access_role.getInquiries_and_reports() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_ACCOUNT_BALANCE_INQ", (access_role != null && access_role.getAccount_balance_inq() != null ) ? access_role.getAccount_balance_inq() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_INTERSET_SUMMARY_INQ", (access_role != null && access_role.getInterset_summary_inq() != null ) ? access_role.getInterset_summary_inq() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_JOURNAL_BOOK", (access_role != null && access_role.getJournal_book() != null ) ? access_role.getJournal_book() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_ACCOUNT_LEDGERS_I", (access_role != null && access_role.getAccount_ledgers_i() != null ) ? access_role.getAccount_ledgers_i() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_TRIAL_BALANCE_I", (access_role != null && access_role.getTrial_balance_i() != null ) ? access_role.getTrial_balance_i() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_GENERAL_LEDGER", (access_role != null && access_role.getGeneral_ledger() != null ) ? access_role.getGeneral_ledger() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_PROFIT_AND_LOSS_ACCOUNT_I", (access_role != null && access_role.getProfit_and_loss_account_i() != null ) ? access_role.getProfit_and_loss_account_i() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_BALANCE_SHEET", (access_role != null && access_role.getBalance_sheet() != null ) ? access_role.getBalance_sheet() : 'N' );
				request.getSession().setAttribute("SCREEN_ACCESS_BALANCE_SHEETS", (access_role != null && access_role.getBalance_sheets() != null ) ? access_role.getBalance_sheets() : 'N' );
                request.getSession().setAttribute("SCREEN_ACCESS_CREDIT_FACILITY_REPORT", (access_role != null && access_role.getCredit_facility_report() != null ) ? access_role.getCredit_facility_report() : 'N' );
                request.getSession().setAttribute("SCREEN_ACCESS_END_OF_MONTH_REPORT", (access_role != null && access_role.getEnd_of_month_report() != null ) ? access_role.getEnd_of_month_report() : 'N' );
                request.getSession().setAttribute("SCREEN_ACCESS_DAB", (access_role != null && access_role.getDab() != null ) ? access_role.getDab() : 'N' );
                request.getSession().setAttribute("SCREEN_ACCESS_CONSOLIDATED_REPORT", (access_role != null && access_role.getConsolidated_report() != null ) ? access_role.getConsolidated_report() : 'N' );
                request.getSession().setAttribute("SCREEN_ACCESS_TRANSACTION_REPORT", (access_role != null && access_role.getTransaction_report() != null ) ? access_role.getTransaction_report() : 'N' );

				audit.insertUserAudit(user.getUserid(), user.getUsername(), "LOGIN", "LOGGED IN SUCCESSFULLY","BGLS_USER_PROFILE_TABLE", "LOGIN ");
				response.sendRedirect("Dashboard");
			}

		};

	}

	@Bean
	public LogoutSuccessHandler xbrlLogoutSuccessHandler() {

		return new LogoutSuccessHandler() {

			@Override
			public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				Optional<UserProfile> up = userProfileRep.findById(authentication.getName());

				UserProfile user = up.get();	
				audit.insertUserAudit(user.getUserid(), user.getUsername(), "LOGOUT", "LOGGED OUT SUCCESSFULLY","BGLS_USER_PROFILE_TABLE", "LOGOUT ");
				response.sendRedirect("login?logout");
			}
		};
	}

}