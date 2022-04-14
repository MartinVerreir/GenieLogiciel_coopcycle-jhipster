import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './panier.reducer';

export const PanierDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const panierEntity = useAppSelector(state => state.panier.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="panierDetailsHeading">
          <Translate contentKey="coopcycleApp.panier.detail.title">Panier</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{panierEntity.id}</dd>
          <dt>
            <span id="montant">
              <Translate contentKey="coopcycleApp.panier.montant">Montant</Translate>
            </span>
          </dt>
          <dd>{panierEntity.montant}</dd>
          <dt>
            <span id="dateLimite">
              <Translate contentKey="coopcycleApp.panier.dateLimite">Date Limite</Translate>
            </span>
          </dt>
          <dd>{panierEntity.dateLimite}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.panier.paiement">Paiement</Translate>
          </dt>
          <dd>{panierEntity.paiement ? panierEntity.paiement.id : ''}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.panier.client">Client</Translate>
          </dt>
          <dd>{panierEntity.client ? panierEntity.client.id : ''}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.panier.commercant">Commercant</Translate>
          </dt>
          <dd>{panierEntity.commercant ? panierEntity.commercant.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/panier" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/panier/${panierEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PanierDetail;
