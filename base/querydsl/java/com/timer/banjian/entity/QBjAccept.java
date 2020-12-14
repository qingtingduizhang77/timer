package com.timer.banjian.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBjAccept is a Querydsl query type for BjAccept
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBjAccept extends EntityPathBase<BjAccept> {

    private static final long serialVersionUID = -1343940558L;

    public static final QBjAccept bjAccept = new QBjAccept("bjAccept");

    public final com.timer.common.entity.QBaseSimpleEntity _super = new com.timer.common.entity.QBaseSimpleEntity(this);

    public final DateTimePath<java.util.Date> acceptDate = createDateTime("acceptDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> applyDate = createDateTime("applyDate", java.util.Date.class);

    public final StringPath applyerName = createString("applyerName");

    public final StringPath applyerPageCode = createString("applyerPageCode");

    public final StringPath applyerPageType = createString("applyerPageType");

    public final StringPath applyerType = createString("applyerType");

    public final StringPath applyForm = createString("applyForm");

    public final StringPath applyType = createString("applyType");

    public final StringPath catalogCode = createString("catalogCode");

    public final StringPath cdBatch = createString("cdBatch");

    public final StringPath cdOperation = createString("cdOperation");

    public final DateTimePath<java.util.Date> cdTime = createDateTime("cdTime", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> created = _super.created;

    public final StringPath handleusername = createString("handleusername");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath itemCode = createString("itemCode");

    //inherited
    public final DateTimePath<java.util.Date> lastmodi = _super.lastmodi;

    public final StringPath localCatalogCode = createString("localCatalogCode");

    public final StringPath localTaskCode = createString("localTaskCode");

    public final StringPath orgCode = createString("orgCode");

    public final StringPath orgName = createString("orgName");

    public final StringPath projectNo = createString("projectNo");

    public final DateTimePath<java.util.Date> promisedate = createDateTime("promisedate", java.util.Date.class);

    public final StringPath rowGuid = createString("rowGuid");

    public final StringPath sblsh = createString("sblsh");

    public final StringPath sblshShort = createString("sblshShort");

    public final StringPath slztdm = createString("slztdm");

    public final StringPath taskCode = createString("taskCode");

    public final StringPath taskHandleItem = createString("taskHandleItem");

    public final StringPath taskName = createString("taskName");

    //inherited
    public final NumberPath<Long> version = _super.version;

    public final StringPath year = createString("year");

    public QBjAccept(String variable) {
        super(BjAccept.class, forVariable(variable));
    }

    public QBjAccept(Path<? extends BjAccept> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBjAccept(PathMetadata metadata) {
        super(BjAccept.class, metadata);
    }

}

