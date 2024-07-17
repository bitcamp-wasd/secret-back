import Eureka from 'eureka-js-client';
import { SpringCloudConfig } from './cloud.config';
import { Injectable, OnModuleInit } from '@nestjs/common';

/**
 * Spring Cloud Eureka Server 연결
 */
@Injectable()
export class EurekaConfig implements OnModuleInit {
  private eurekaClient: Eureka;

  constructor(private readonly springCloudConfig: SpringCloudConfig) {}
  async onModuleInit() {
    this.eurekaClient = new Eureka({
      instance: {
        app: 'challenge',
        hostName: this.springCloudConfig.get('Eureka.instance.hostName'),
        ipAddr: this.springCloudConfig.get('Eureka.instance.ipAddr'),
        vipAddress: 'challenge',
        port: {
          $: this.springCloudConfig.get('Eureka.instance.port'),
          '@enabled': true,
        },
        dataCenterInfo: {
          name: 'MyOwn',
          '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
        },
      },
      eureka: {
        host: this.springCloudConfig.get('Eureka.eureka.host'),
        port: this.springCloudConfig.get('Eureka.eureka.port'),
        servicePath: '/eureka/apps/',
      },
    });
  }

  start() {
    this.eurekaClient.start();
  }
}
