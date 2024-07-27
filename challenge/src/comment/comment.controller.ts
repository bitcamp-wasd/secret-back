import {
  Body,
  Controller,
  Delete,
  Get,
  Patch,
  Post,
  Query,
} from '@nestjs/common';
import { UserAuth } from 'src/auth/headers.decorator';
import { UserAuthDto } from 'src/auth/userAuth.dto';
import { CreateCommentReqDto } from './dto/request/createComment.request';
import { CommentService } from './comment.service';
import { PatchCommentReqDto } from './dto/request/patchComment.request';
import { DeleteCommentReqDto } from './dto/request/deleteComment.request';

@Controller()
export class CommentController {
  constructor(private readonly commentService: CommentService) {}

  @Post('auth/comment')
  async createComment(
    @UserAuth('user') userAuth: UserAuthDto,
    @Body() createComment: CreateCommentReqDto,
  ) {
    this.commentService.createComment(userAuth, createComment);
  }

  @Get('comment')
  async getComment(
    @Query('videoId') videoId: string,
    @Query('pageNumber') pageNumber: number,
  ) {
    return await this.commentService.getVideoComment(videoId, pageNumber);
  }

  @Patch('auth/comment')
  async patchComment(
    @UserAuth('user') userAuth: UserAuthDto,
    @Query('commentId') commentId: string,
    @Body() comment: PatchCommentReqDto,
  ) {
    await this.commentService.patchComment(userAuth, commentId, comment);
  }

  @Delete('auth/comment')
  async deleteComment(
    @UserAuth('user') userAuth: UserAuthDto,
    @Body() deleteComment: DeleteCommentReqDto,
  ) {
    await this.commentService.deleteComment(userAuth, deleteComment);
  }

  @Get('auth/mycomment')
  async getMyComment(
    @UserAuth('user') userAuth: UserAuthDto,
    @Query('pageNumber') pageNumber: number,
  ) {
    return await this.commentService.myComment(userAuth, pageNumber);
  }
}
