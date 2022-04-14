import { ICommande } from 'app/shared/model/commande.model';
import { IPanier } from 'app/shared/model/panier.model';

export interface IPaiement {
  id?: number;
  montant?: number;
  commande?: ICommande | null;
  panier?: IPanier | null;
}

export const defaultValue: Readonly<IPaiement> = {};
