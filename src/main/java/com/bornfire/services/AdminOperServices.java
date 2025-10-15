package com.bornfire.services;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.bornfire.entities.Access_Role_Entity;
import com.bornfire.entities.BGLSBusinessTable_Entity;
import com.bornfire.entities.BGLSBusinessTable_Rep;
import com.bornfire.entities.BamDocumentMasRep;
import com.bornfire.entities.Bamdocumentmanager;
import com.bornfire.entities.Chart_Acc_Entity;
import com.bornfire.entities.Chart_Acc_Rep;
import com.bornfire.entities.GeneralLedgerEntity;
import com.bornfire.entities.GeneralLedgerRep;
import com.bornfire.entities.UserProfile;
import com.bornfire.entities.UserProfileRep;
import com.ibm.icu.text.SimpleDateFormat;

@Service
public class AdminOperServices {

	@Autowired
	GeneralLedgerRep generalLedgerRep;

	@Autowired
	BamDocumentMasRep BamDocmasRep;

	@Autowired
	Chart_Acc_Rep chart_Acc_Rep;

	@Autowired
	BGLSBusinessTable_Rep bGLSBusinessTable_Rep;

	@Autowired
	UserProfileRep userProfileRep;

	@Autowired
	AuditConfigure audit1;

	public Chart_Acc_Entity getGeneralLedger(String acct_num) {

		/*
		 * if (generalLedgerRep.existsById(id)) { GeneralLedgerEntity up =
		 * generalLedgerRep.findById(id).get(); return up; } else { return new
		 * GeneralLedgerEntity(); }
		 */
		return chart_Acc_Rep.getaedit(acct_num);
	}

	public String addGeneralLedger(GeneralLedgerEntity getGeneralLedger, String formmode, String GL_CODE,
			String glsh_code, String userid, HttpServletRequest rq) {

		String msg = "";
		BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
		if (formmode.equals("add")) {

			GeneralLedgerEntity up = getGeneralLedger;

			up.setDelFlg("N");
			up.setModifyFlg("N");
			up.setEntry_user(userid);
			// up.setEntry_time(sdf.format(new Date()));
			up.setEntry_time(new Date());
			generalLedgerRep.save(up);

			msg = "Added Successfully";

			// FOR AUIDT
			Optional<UserProfile> up1 = userProfileRep.findById(userid);
			UserProfile user = up1.get();

			audit1.insertServiceAudit(user.getUserid(), user.getUsername(), "GENERAL LEDGER ADD", "ADDED SUCCESSFULLY",
					"BGLS_GENERAL_LED", "GENERAL LEDGER","-");

		} else if (formmode.equals("edit")) {
		    System.out.println("The GL Code being modified: " + getGeneralLedger.getGlsh_code());

		    String userid1 = (String) rq.getSession().getAttribute("USERID");
		    String msg1 = "";

		    // Fetch existing record from DB
		    Optional<GeneralLedgerEntity> existingOpt = generalLedgerRep.findById(getGeneralLedger.getGlsh_code());

		    if (existingOpt.isPresent()) {
		        GeneralLedgerEntity existing = existingOpt.get();
		        GeneralLedgerEntity updated = getGeneralLedger;

		        StringBuilder changeDetails = new StringBuilder();

		        // Compare and log modified fields
		        if (!Objects.equals(existing.getGlCode(), updated.getGlCode())) {
		            changeDetails.append("GL Code+").append(existing.getGlCode()).append("+")
		                         .append(updated.getGlCode()).append("||");
		        }
		        if (!Objects.equals(existing.getGlDescription(), updated.getGlDescription())) {
		            changeDetails.append("GL Description+").append(existing.getGlDescription()).append("+")
		                         .append(updated.getGlDescription()).append("||");
		        }
		        if (!Objects.equals(existing.getRemarks(), updated.getRemarks())) {
		            changeDetails.append("Remarks+").append(existing.getRemarks()).append("+")
		                         .append(updated.getRemarks()).append("||");
		        }
		        if (!Objects.equals(existing.getBranch_id(), updated.getBranch_id())) {
		            changeDetails.append("Branch ID+").append(existing.getBranch_id()).append("+")
		                         .append(updated.getBranch_id()).append("||");
		        }
		        if (!Objects.equals(existing.getBranch_desc(), updated.getBranch_desc())) {
		            changeDetails.append("Branch Description+").append(existing.getBranch_desc()).append("+")
		                         .append(updated.getBranch_desc()).append("||");
		        }
		        if (!Objects.equals(existing.getGlsh_desc(), updated.getGlsh_desc())) {
		            changeDetails.append("GLSH Description+").append(existing.getGlsh_desc()).append("+")
		                         .append(updated.getGlsh_desc()).append("||");
		        }
		        if (!Objects.equals(existing.getCrncy_code(), updated.getCrncy_code())) {
		            changeDetails.append("Currency Code+").append(existing.getCrncy_code()).append("+")
		                         .append(updated.getCrncy_code()).append("||");
		        }
		        if (!Objects.equals(existing.getBal_sheet_group(), updated.getBal_sheet_group())) {
		            changeDetails.append("Balance Sheet Group+").append(existing.getBal_sheet_group()).append("+")
		                         .append(updated.getBal_sheet_group()).append("||");
		        }
		        if (!Objects.equals(existing.getSeq_order(), updated.getSeq_order())) {
		            changeDetails.append("Sequence Order+").append(existing.getSeq_order()).append("+")
		                         .append(updated.getSeq_order()).append("||");
		        }
		        if (!Objects.equals(existing.getGl_type(), updated.getGl_type())) {
		            changeDetails.append("GL Type+").append(existing.getGl_type()).append("+")
		                         .append(updated.getGl_type()).append("||");
		        }
		        if (!Objects.equals(existing.getGl_type_description(), updated.getGl_type_description())) {
		            changeDetails.append("GL Type Description+").append(existing.getGl_type_description()).append("+")
		                         .append(updated.getGl_type_description()).append("||");
		        }
		        if (!Objects.equals(existing.getModule(), updated.getModule())) {
		            changeDetails.append("Module+").append(existing.getModule()).append("+")
		                         .append(updated.getModule()).append("||");
		        }
		        if (!Objects.equals(existing.getTotal_balance(), updated.getTotal_balance())) {
		            changeDetails.append("Total Balance+").append(existing.getTotal_balance()).append("+")
		                         .append(updated.getTotal_balance()).append("||");
		        }
		        if (!Objects.equals(existing.getNo_acct_opened(), updated.getNo_acct_opened())) {
		            changeDetails.append("No. of Accounts Opened+").append(existing.getNo_acct_opened()).append("+")
		                         .append(updated.getNo_acct_opened()).append("||");
		        }
		        if (!Objects.equals(existing.getNo_acct_closed(), updated.getNo_acct_closed())) {
		            changeDetails.append("No. of Accounts Closed+").append(existing.getNo_acct_closed()).append("+")
		                         .append(updated.getNo_acct_closed()).append("||");
		        }

		        // Update record
		        existing.setGlCode(updated.getGlCode());
		        existing.setGlDescription(updated.getGlDescription());
		        existing.setRemarks(updated.getRemarks());
		        existing.setBranch_id(updated.getBranch_id());
		        existing.setBranch_desc(updated.getBranch_desc());
		        existing.setGlsh_desc(updated.getGlsh_desc());
		        existing.setCrncy_code(updated.getCrncy_code());
		        existing.setBal_sheet_group(updated.getBal_sheet_group());
		        existing.setSeq_order(updated.getSeq_order());
		        existing.setGl_type(updated.getGl_type());
		        existing.setGl_type_description(updated.getGl_type_description());
		        existing.setModule(updated.getModule());
		        existing.setTotal_balance(updated.getTotal_balance());
		        existing.setNo_acct_opened(updated.getNo_acct_opened());
		        existing.setNo_acct_closed(updated.getNo_acct_closed());

		        existing.setModifyFlg("Y");
		        existing.setDelFlg("N");
		        existing.setModify_user(userid);
		        existing.setModify_time(new Date());

		        generalLedgerRep.save(existing);
		        msg = "Modified Successfully";

		        // ✅ Audit Section
		        Optional<UserProfile> up1 = userProfileRep.findById(userid);
		        if (up1.isPresent()) {
		            UserProfile user = up1.get();
		            audit1.insertServiceAudit(
		                user.getUserid(),
		                user.getUsername(),
		                "GENERAL LEDGER EDIT",
		                "EDITED SUCCESSFULLY",
		                "BGLS_GENERAL_LED",
		                "GENERAL LEDGER",
		                "FIELDS MODIFIED: " + (changeDetails.length() > 0 ? changeDetails.toString() : "No field changes")
		            );
		        }
		    } else {
		        msg = "Record not found for modification.";
		    }

		    return msg;
		} else if (formmode.equals("delete")) {
			System.out.println("the getting gl code is " + GL_CODE);
			GeneralLedgerEntity up = generalLedgerRep.getRefMaster(getGeneralLedger.getGlsh_code());
			up.setDelFlg("Y");
			Optional<UserProfile> up1 = userProfileRep.findById(userid);
			UserProfile user = up1.get();
			audit1.insertServiceAudit(user.getUserid(), user.getUsername(), "GENERAL LEDGER DELETE",
					"DELETED SUCCESSFULLY", "BGLS_GENERAL_LED", "GENERAL LEDGER","-");
			generalLedgerRep.save(up);
			msg = "Deleted Successfully";
		}

		return msg;
	}

	// @Value("${document.folder.path}")
	private String documentFolderPath;

	public String DocManaaddedit(Bamdocumentmanager Bamdocumentmanager, String formmode, MultipartFile file) {
		String msg = "";

		try {
			if (formmode.equals("edit")) {
				Optional<Bamdocumentmanager> up = BamDocmasRep.findById(Bamdocumentmanager.getDoc_id());

				if (up.isPresent()) {
					Bamdocumentmanager bamcat = up.get();
					if (file != null) {
						String filePath = saveFile(file, bamcat.getDoc_id());
						bamcat.setDoc_location(filePath);
					}
					bamcat.setModify_time(new Date());
					bamcat.setDel_flg("N");
					BamDocmasRep.save(bamcat);
					msg = "Modified Successfully";
				}
			} else if (formmode.equals("add")) {
				if (file != null) {
					String filePath = saveFile(file, Bamdocumentmanager.getDoc_id());
					System.out.println(filePath);
					Bamdocumentmanager.setDoc_location(filePath);
				}
				Bamdocumentmanager.setDel_flg("N");
				BamDocmasRep.save(Bamdocumentmanager);
				msg = "Added Successfully";
			} else if (formmode.equals("verify")) {
				Optional<Bamdocumentmanager> up = BamDocmasRep.findById(Bamdocumentmanager.getDoc_id());

				if (up.isPresent()) {
					Bamdocumentmanager bamcat = up.get();
					bamcat.setDel_flg("Y");
					BamDocmasRep.save(bamcat);
					msg = "Verified Successfully";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			msg = "Document Upload Unsuccessful";
		}

		return msg;
	}

	private String saveFile(MultipartFile file, String docId) throws IOException {
		String fileName = docId + "_" + file.getOriginalFilename();
		String filePath = documentFolderPath + File.separator + fileName;
		File destinationFile = new File(filePath);
		file.transferTo(destinationFile);
		return filePath;
	}

	@Autowired
	SessionFactory sessionFactory;

	public Access_Role_Entity getRoleMenu(String id) {
		Session session = sessionFactory.getCurrentSession();
		Query<Access_Role_Entity> query = session.createQuery("from Access_Role_Entity where role_id = :role_id",
				Access_Role_Entity.class);
		query.setParameter("role_id", id);

		List<Access_Role_Entity> result = query.getResultList();
		if (!result.isEmpty()) {
			return result.get(0);
		} else {
			return new Access_Role_Entity();
		}
	}

}
