import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './client.reducer';

export const ClientDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const clientEntity = useAppSelector(state => state.client.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clientDetailsHeading">
          <Translate contentKey="coopcycleApp.client.detail.title">Client</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{clientEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="coopcycleApp.client.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{clientEntity.nom}</dd>
          <dt>
            <span id="prenom">
              <Translate contentKey="coopcycleApp.client.prenom">Prenom</Translate>
            </span>
          </dt>
          <dd>{clientEntity.prenom}</dd>
          <dt>
            <span id="mail">
              <Translate contentKey="coopcycleApp.client.mail">Mail</Translate>
            </span>
          </dt>
          <dd>{clientEntity.mail}</dd>
          <dt>
            <span id="tel">
              <Translate contentKey="coopcycleApp.client.tel">Tel</Translate>
            </span>
          </dt>
          <dd>{clientEntity.tel}</dd>
          <dt>
            <span id="addresse">
              <Translate contentKey="coopcycleApp.client.addresse">Addresse</Translate>
            </span>
          </dt>
          <dd>{clientEntity.addresse}</dd>
        </dl>
        <Button tag={Link} to="/client" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/client/${clientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClientDetail;
