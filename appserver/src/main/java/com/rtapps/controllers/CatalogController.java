package com.rtapps.controllers;

import com.rtapps.aws.S3Wrapper;
import com.rtapps.controllers.CatalogController.ExistingCatalogImages.ExistingCatalogImage;
import com.rtapps.db.mongo.data.CatalogImage;
import com.rtapps.db.mongo.repository.AdminUserRepository;
import com.rtapps.db.mongo.repository.CatalogImageRepository;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

	private CatalogImage addCatalogImage(String applicationId, MultipartFile fullImage, int index){
		ObjectId objectId = new ObjectId();
		CatalogImage catalogImage = new CatalogImage(objectId.toHexString(), applicationId, myFileServerPath, fullImage.getOriginalFilename(), index);

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
			@RequestParam(value = "fullCatalogImage") MultipartFile [] fullImage,
			@RequestParam(value = "index") int index
			){
		return addCatalogImage(applicationId, fullImage[0], index);
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

	public static class NewCatalogImages{
		private int index;
		public int getIndex(){
			return this.index;
		}

        public void setIndex(int index){
            this.index = index;
        }
	}

	public static class ExistingCatalogImages{

        public List<ExistingCatalogImage> getExistingCatalogImages(){
            return this.existingCatalogImages;
        }

        public void setExistingCatalogImages(List<ExistingCatalogImage> existingCatalogImages){
            this.existingCatalogImages = existingCatalogImages;
        }

        private List <ExistingCatalogImage> existingCatalogImages;

        public static class ExistingCatalogImage

        {
            private String id;
            private int index;

            public String getId() {
            return this.id;
        }

            public int getIndex() {
                return this.index;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setIndex(int index) {
                this.index = index;
            }
        }
	}


	@RequestMapping(value = "/updateCatalog", method = RequestMethod.POST)
	@ResponseBody
	public String updateCatalog(
			@RequestParam(value = "applicationId") String applicationId,
			@RequestParam(value = "existingCatalogImages", required = false) String exitingCatalogImagesStr,
			@RequestParam(value = "newCatalogImageIndexes", required = false) String newCatalogIndexesStr,
			@RequestParam(value = "newCatalogImageFile1", required = false) MultipartFile newCatalogImageFile1,
			@RequestParam(value = "newCatalogImageFile2", required = false) MultipartFile newCatalogImageFile2,
			@RequestParam(value = "newCatalogImageFile3", required = false) MultipartFile newCatalogImageFile3,
			@RequestParam(value = "newCatalogImageFile4", required = false) MultipartFile newCatalogImageFile4,
			@RequestParam(value = "newCatalogImageFile5", required = false) MultipartFile newCatalogImageFile5,
			@RequestParam(value = "newCatalogImageFile6", required = false) MultipartFile newCatalogImageFile6,
			@RequestParam(value = "newCatalogImageFile7", required = false) MultipartFile newCatalogImageFile7
	) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

		MultipartFile [] multipartFiles = new MultipartFile[]{newCatalogImageFile1, newCatalogImageFile2, newCatalogImageFile3, newCatalogImageFile4, newCatalogImageFile5, newCatalogImageFile6, newCatalogImageFile7};

        List<ExistingCatalogImage> exitingCatalogImages = objectMapper.readValue(exitingCatalogImagesStr, ExistingCatalogImages.class).getExistingCatalogImages();

		List<Integer> newCatalogIndexes = objectMapper.readValue(newCatalogIndexesStr, new TypeReference<List<Integer>>(){});

		cleanRemovedCatalogImages(applicationId, exitingCatalogImages);

		for (int i=0; i< newCatalogIndexes.size(); i++){
			addCatalogImage(applicationId, multipartFiles[i], newCatalogIndexes.get(i));
		}


		for (ExistingCatalogImage existingCatalogImage: exitingCatalogImages){
			updateCatalogImageIndex(existingCatalogImage.getId(), applicationId, existingCatalogImage.getIndex());
		}
		return "";
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
			if (existingCatalogImage.getId().equals(catalogImage.getId())){
				return true;
			}
		}
		return false;
	}
}
