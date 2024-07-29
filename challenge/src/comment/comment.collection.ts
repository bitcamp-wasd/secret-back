import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Types } from 'mongoose';
import * as mongoosePaginate from 'mongoose-paginate-v2';

@Schema({ collection: 'comment' })
export class Comment {
  @Prop()
  userId: number;

  @Prop()
  comment: string;

  @Prop({ type: Types.ObjectId, ref: 'ChallengeList' })
  videoId: Types.ObjectId;
}

const schema = SchemaFactory.createForClass(Comment);
schema.set('timestamps', { createdAt: true });
schema.plugin(mongoosePaginate);
export const CommentSchema = schema;
