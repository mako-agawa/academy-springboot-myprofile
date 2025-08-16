package com.spring.springbootapplication.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Service
public class ImageStorageService {

  private final Cloudinary cloudinary;

  public ImageStorageService(Cloudinary cloudinary) {
    this.cloudinary = cloudinary;
  }

  public UploadResult uploadUserAvatar(MultipartFile file, Long userId) throws Exception {
    String original = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
    String ext = (original != null && original.contains(".")) ? original.substring(original.lastIndexOf('.') + 1) : "jpg";

    // public_id を自前で決めると、後から同URLで上書きできる（overwrite:true）
    String publicId = "avatars/" + userId + "/" + UUID.randomUUID();

    Map<?,?> res = cloudinary.uploader().upload(
        file.getBytes(),
        ObjectUtils.asMap(
            "public_id", publicId,
            "folder", "avatars/" + userId, // コンソールで見やすいように
            "overwrite", true,
            "resource_type", "image"
        )
    );

    String secureUrl = (String) res.get("secure_url"); // https URL
    String returnedPublicId = (String) res.get("public_id");

    return new UploadResult(returnedPublicId, secureUrl, ext);
  }

  public void deleteByPublicId(String publicId) throws Exception {
    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
  }

  public record UploadResult(String publicId, String secureUrl, String ext) {}
}