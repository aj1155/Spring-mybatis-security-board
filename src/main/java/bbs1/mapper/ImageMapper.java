package bbs1.mapper;

import bbs1.dto.Image;

public interface ImageMapper {
	Image selectById(int id);
	void insert(Image image);
	void deleteOrphan();
}
