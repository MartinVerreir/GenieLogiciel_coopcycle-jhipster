import { IPaiement } from 'app/shared/model/paiement.model';
import { ILivreur } from 'app/shared/model/livreur.model';
import { IClient } from 'app/shared/model/client.model';

export interface ICommande {
  id?: number;
  adresseLivraison?: string;
  echeance?: number;
  paiement?: IPaiement | null;
  livreur?: ILivreur | null;
  client?: IClient | null;
}

export const defaultValue: Readonly<ICommande> = {};
