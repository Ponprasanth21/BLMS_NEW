package com.bornfire.services;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
			String glsh_code, String userid) {

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
					"BGLS_GENERAL_LED", "GENERAL LEDGER");

		} else if (formmode.equals("edit")) {
			System.out.println("the getting  glsh code is " + getGeneralLedger.getGlsh_code());

			GeneralLedgerEntity up = getGeneralLedger;

			up.setGlCode(getGeneralLedger.getGlCode());
			up.setGlDescription(getGeneralLedger.getGlDescription());
			up.setModifyFlg("Y");
			up.setDelFlg("N");
			up.setModify_user(userid);
			// up.setModify_time(sdf.format(new Date()));
			up.setModify_time(new Date());
			generalLedgerRep.save(up);

			// FOR AUIDT
			Optional<UserProfile> up1 = userProfileRep.findById(userid);
			UserProfile user = up1.get();
			audit1.insertServiceAudit(user.getUserid(), user.getUsername(), "GENERAL LEDGER EDIT",
					"EDITED SUCCESSFULLY", "BGLS_GENERAL_LED", "GENERAL LEDGER");
			msg = "Modify Successfully";

			return msg;
		} else if (formmode.equals("delete")) {
			System.out.println("the getting gl code is " + GL_CODE);
			GeneralLedgerEntity up = generalLedgerRep.getRefMaster(getGeneralLedger.getGlsh_code());
			up.setDelFlg("Y");
			Optional<UserProfile> up1 = userProfileRep.findById(userid);
			UserProfile user = up1.get();
			audit1.insertServiceAudit(user.getUserid(), user.getUsername(), "GENERAL LEDGER DELETE",
					"DELETED SUCCESSFULLY", "BGLS_GENERAL_LED", "GENERAL LEDGER");
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
