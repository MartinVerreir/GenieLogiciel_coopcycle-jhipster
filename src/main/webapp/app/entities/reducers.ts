import client from 'app/entities/client/client.reducer';
import panier from 'app/entities/panier/panier.reducer';
import paiement from 'app/entities/paiement/paiement.reducer';
import livreur from 'app/entities/livreur/livreur.reducer';
import cooperative from 'app/entities/cooperative/cooperative.reducer';
import commercant from 'app/entities/commercant/commercant.reducer';
import commande from 'app/entities/commande/commande.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  client,
  panier,
  paiement,
  livreur,
  cooperative,
  commercant,
  commande,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
