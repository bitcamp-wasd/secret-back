import { Inject, Injectable, Logger } from '@nestjs/common';
import { Config } from 'cloud-config-client';

/**
 * Spring Cloud Config Server에서 설정값 가져오기
 */
@Injectable()
export class SpringCloudConfig {
  private readonly logger: Logger = new Logger(SpringCloudConfig.name);
  @Inject('config')
  private config: Config;

  get(variable: string): string {
    const value: string = this.config.get(variable);

    this.logger.debug(variable + ': ' + value);
    return value;
  }
}
