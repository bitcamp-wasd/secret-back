import { Model } from 'mongoose';
import { Vote } from './vote.collection';
import { Injectable, NotFoundException } from '@nestjs/common';
import { UserAuthDto } from 'src/auth/userAuth.dto';
import { VoteRequestDto } from './dto/request/voteRequest.dto';
import { InjectModel } from '@nestjs/mongoose';

@Injectable()
export class VoteService {
  constructor(@InjectModel('Vote') private readonly voteModel: Model<Vote>) {}

  async vote(userAuth: UserAuthDto, voteRequestDto: VoteRequestDto) {
    if (await this.checkVote(userAuth, voteRequestDto))
      throw new NotFoundException('이미 투표한 상태입니다.');

    const vote = new this.voteModel({
      userId: userAuth.userId,
      challengeId: voteRequestDto.challengeId,
      challengeListId: voteRequestDto.challengeListId,
    });

    this.voteModel.create(vote);
  }

  async checkVote(
    userAuth: UserAuthDto,
    voteRequestDto: VoteRequestDto,
  ): Promise<boolean> {
    const { challengeId, challengeListId } = voteRequestDto;

    const exist = await this.voteModel.exists({
      challengeId: challengeId,
      challengeListId: challengeListId,
      userId: userAuth.userId,
    });

    if (exist !== null) return true;
    return false;
  }
}
