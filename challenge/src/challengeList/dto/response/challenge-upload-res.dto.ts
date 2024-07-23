export class ChallengeUploadResponseDto {
  videoPresignedUrl: string;
  thumbnailPresignedUrl: string;

  constructor(videoPresignedUrl: string, thumbnailPresignedUrl: string) {
    this.videoPresignedUrl = videoPresignedUrl;
    this.thumbnailPresignedUrl = thumbnailPresignedUrl;
  }
}
