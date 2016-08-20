package com.rtapps.controllers;

import com.rtapps.aws.S3Wrapper;
import com.rtapps.db.mongo.data.CatalogImage;
import com.rtapps.db.mongo.repository.AdminUserRepository;
import com.rtapps.db.mongo.repository.CatalogImageRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by rtichauer on 8/20/16.
 */

@Controller
public class CatalogController {

	@Autowired
	AdminUserRepository adminUserRepostory;

	@Autowired
	CatalogImageRepository catalogImageRepository;

	@Autowired
	private S3Wrapper s3Wrapper;

	@Value("${cloud.aws.s3.myFileServerPath}")
	private String myFileServerPath;

	@RequestMapping(value = "/putCatalogImage", method = RequestMethod.POST)
	@ResponseBody
	public CatalogImage putCatalogImage(
			@RequestParam(value = "applicationId") String applicationId,
			@RequestParam(value = "fullCatalogImage") MultipartFile[] fullImage){

		ObjectId objectId = new ObjectId();
		CatalogImage catalogImage = new CatalogImage(objectId.toHexString(), applicationId, myFileServerPath, fullImage[0].getOriginalFilename());


		s3Wrapper.upload(fullImage, "images/" + applicationId + "/" + catalogImage.getId() + "/");


		 catalogImage = catalogImageRepository.save(catalogImage);


		return catalogImage;
	}

	@RequestMapping(value = "/deleteCatalogImage", method = RequestMethod.POST)
	@ResponseBody
	public CatalogImage deleteMessage(
			@RequestParam(value = "applicationId", required = true) String applicationId,
			@RequestParam(value = "catalogId", required = true) String catalogId){

		CatalogImage catalogImage = catalogImageRepository.findByIdAndApplicationId(catalogId, applicationId);

		if (catalogImage!= null)
		{
			String fullImageName = "images/" + applicationId + "/" + catalogImage.getId() + "/" + catalogImage.getFullImageName();
			s3Wrapper.delete(fullImageName);
		}
		return catalogImage;
	}

	@RequestMapping(value = "/catalog", method = RequestMethod.GET)
	@ResponseBody
	public List<CatalogImage> messages(
			@RequestParam(value = "applicationId") String applicationId) {

		List<CatalogImage> catalogImageList = catalogImageRepository.findByApplicationId(applicationId);

		return catalogImageList;
	}
}
