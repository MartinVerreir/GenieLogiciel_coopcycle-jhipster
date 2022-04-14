import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './livreur.reducer';

export const LivreurDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const livreurEntity = useAppSelector(state => state.livreur.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="livreurDetailsHeading">
          <Translate contentKey="coopcycleApp.livreur.detail.title">Livreur</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="coopcycleApp.livreur.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.nom}</dd>
          <dt>
            <span id="prenom">
              <Translate contentKey="coopcycleApp.livreur.prenom">Prenom</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.prenom}</dd>
          <dt>
            <span id="tel">
              <Translate contentKey="coopcycleApp.livreur.tel">Tel</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.tel}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.livreur.cooperative">Cooperative</Translate>
          </dt>
          <dd>{livreurEntity.cooperative ? livreurEntity.cooperative.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/livreur" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/livreur/${livreurEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LivreurDetail;
