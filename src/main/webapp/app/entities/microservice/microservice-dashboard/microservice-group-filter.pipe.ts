import { Pipe, PipeTransform } from '@angular/core';
import { IMicroservice } from 'app/shared/model/microservice.model';
import { IMicroserviceGroupFilter } from 'app/shared/model/util/microservice-group-filter';

@Pipe({
  name: 'microserviceGroupFilter',
})
export class MicroserviceGroupFilterPipe implements PipeTransform {
  transform(items: IMicroservice[], filter: IMicroserviceGroupFilter): IMicroservice[] {
    if (!items || !filter) {
      return items;
    }

    return items.filter(i => {
      let match = true;
      match = match && this.contains(filter.caseSensitive, i.name, filter.name);
      match = match && this.contains(filter.caseSensitive, i.description, filter.description);
      match = match && this.contains(filter.caseSensitive, i.swaggerUrl, filter.swaggerUrl);
      match = match && this.contains(filter.caseSensitive, i.gitUrl, filter.gitUrl);

      if (filter.status && i.status) {
        match = match && i.status.id === filter.status.id;
      }
      if (filter.team && i.team) {
        match = match && i.team.id === filter.team.id;
      }

      return match;
    });
  }

  contains(caseSensitive: boolean, source?: string, substr?: string): boolean {
    if (!substr) {
      return true;
    }

    if (!source && substr) {
      return false;
    }

    if (source && substr) {
      let sourceTerm = source;
      let targetTerm = substr;
      if (!caseSensitive) {
        sourceTerm = source.toLowerCase();
        targetTerm = substr.toLowerCase();
      }

      return sourceTerm.includes(targetTerm);
    }
    return false;
  }
}
