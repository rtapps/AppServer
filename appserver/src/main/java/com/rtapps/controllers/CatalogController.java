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

	private CatalogImage addCatalogImage(String applicationId, MultipartFile[] fullImage, int index){
		ObjectId objectId = new ObjectId();
		CatalogImage catalogImage = new CatalogImage(objectId.toHexString(), applicationId, myFileServerPath, fullImage[0].getOriginalFilename(), index);

		s3Wrapper.upload(fullImage, "images/" + applicationId + "/" + catalogImage.getId() + "/");

		return catalogImageRepository.save(catalogImage);
	}

	private CatalogImage updateCatalogImageIndex(String catalogImageId, String applicationId, int index){
		CatalogImage catalogImage = catalogImageRepository.findByIdAndApplicationId(catalogImageId, applicationId);
		if (catalogImage != null){
			catalogImage.setIndex(index);
		}
		return catalogImageRepository.save(catalogImage);
	}

	@RequestMapping(value = "/putCatalogImage", method = RequestMethod.POST)
	@ResponseBody
	public CatalogImage putCatalogImage(
			@RequestParam(value = "applicationId") String applicationId,
			@RequestParam(value = "fullCatalogImage") MultipartFile[] fullImage,
			@RequestParam(value = "index") int index
			){
		return addCatalogImage(applicationId, fullImage, index);
	}

	@RequestMapping(value = "/deleteCatalogImage", method = RequestMethod.POST)
	@ResponseBody
	public CatalogImage deleteCatalogImage(
			@RequestParam(value = "applicationId", required = true) String applicationId,
			@RequestParam(value = "catalogId", required = true) String catalogId){

		CatalogImage catalogImage = catalogImageRepository.findByIdAndApplicationId(catalogId, applicationId);

		if (catalogImage!= null)
		{
			String fullImageName = "images/" + applicationId + "/" + catalogImage.getId() + "/" + catalogImage.getFullImageName();
			s3Wrapper.delete(fullImageName);
		}
		catalogImageRepository.delete(catalogImage);
		return catalogImage;
	}

	@RequestMapping(value = "/catalog", method = RequestMethod.GET)
	@ResponseBody
	public List<CatalogImage> messages(
			@RequestParam(value = "applicationId") String applicationId) {

		List<CatalogImage> catalogImageList = catalogImageRepository.findByApplicationId(applicationId);

		return catalogImageList;
	}

	private static class NewCatalogImage{
		private int index;
		private MultipartFile[] fullImage;

		public NewCatalogImage(int index, MultipartFile [] fullImage){
			this.index = index;
			this.fullImage = fullImage;
		}

		public int getIndex(){
			return this.index;
		}

		public MultipartFile[] getFullImage(){
			return this.fullImage;
		}
	}

	private static class ExistingCatalogImage{
		private String id;
		private int index;

		public ExistingCatalogImage(String id, int index){
			this.id = id;
			this.index = index;
		}

		public String getId(){
			return this.id;
		}

		public int getIndex(){
			return this.index;
		}
	}


	@RequestMapping(value = "/updateCatalog", method = RequestMethod.POST)
	@ResponseBody
	public List<CatalogImage> updateCatalog(
			@RequestParam(value = "applicationId") String applicationId,
			@RequestParam(value = "newCatalogImages") List<NewCatalogImage> newCatalogImages,
			@RequestParam(value = "existingCatalogImages") List<ExistingCatalogImage> exitingCatalogImages){

		cleanRemovedCatalogImages(applicationId, exitingCatalogImages);

		for (NewCatalogImage image: newCatalogImages){
			addCatalogImage(applicationId, image.getFullImage(), image.getIndex());
		}

		for (ExistingCatalogImage existingCatalogImage: exitingCatalogImages){
			updateCatalogImageIndex(existingCatalogImage.getId(), applicationId, existingCatalogImage.getIndex());
		}
		return catalogImageRepository.findByApplicationId(applicationId);
	}

	private void cleanRemovedCatalogImages(String applicationId, List<ExistingCatalogImage> exitingCatalogImages) {
		List<CatalogImage> catalogImages = catalogImageRepository.findByApplicationId(applicationId);
		catalogImages.forEach(catalogImage -> {
			if (!stillExists(catalogImage, exitingCatalogImages)){
				String fullImageName = "images/" + applicationId + "/" + catalogImage.getId() + "/" + catalogImage.getFullImageName();
				s3Wrapper.delete(fullImageName);
				catalogImageRepository.delete(catalogImage);
			}
		});
	}

	private boolean stillExists(CatalogImage catalogImage, List<ExistingCatalogImage> exitingCatalogImages){
		for (ExistingCatalogImage existingCatalogImage: exitingCatalogImages){
			if (existingCatalogImage.getId() == catalogImage.getId()){
				return true;
			}
		}
		return false;
	}
}
