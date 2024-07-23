import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Types } from 'mongoose';

@Schema({ collection: 'vote' })
export class Vote {
  @Prop()
  userId: number;

  @Prop()
  challengeId: Types.ObjectId;

  @Prop()
  challengeListId: Types.ObjectId;
}

export const VoteSchema = SchemaFactory.createForClass(Vote);
