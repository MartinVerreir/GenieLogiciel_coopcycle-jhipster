import { IPaiement } from 'app/shared/model/paiement.model';
import { IClient } from 'app/shared/model/client.model';
import { ICommercant } from 'app/shared/model/commercant.model';

export interface IPanier {
  id?: number;
  montant?: number;
  dateLimite?: number;
  paiement?: IPaiement | null;
  client?: IClient | null;
  commercant?: ICommercant | null;
}

export const defaultValue: Readonly<IPanier> = {};
