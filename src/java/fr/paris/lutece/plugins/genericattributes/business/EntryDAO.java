/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.genericattributes.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for Entry objects
 */
public final class EntryDAO implements IEntryDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT MAX( id_entry ) FROM genatt_entry";
    private static final String SQL_QUERY_SELECT_ENTRY_ATTRIBUTES = "SELECT ent.id_type,typ.title,typ.is_group,typ.is_comment,typ.class_name,typ.is_mylutece_user," +
        "ent.id_entry,ent.id_resource,ent.resource_type,ent.id_parent,ent.title,ent.help_message," +
        "ent.comment,ent.mandatory,ent.fields_in_line," +
        "ent.pos,ent.id_field_depend,ent.confirm_field,ent.confirm_field_title,ent.field_unique, ent.map_provider, ent.css_class, ent.pos_conditional, ent.error_message " +
        "FROM genatt_entry ent,genatt_entry_type typ WHERE ent.id_type=typ.id_type ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = SQL_QUERY_SELECT_ENTRY_ATTRIBUTES +
        " AND ent.id_entry = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO genatt_entry ( " +
        "id_entry,id_resource,resource_type,id_type,id_parent,title,help_message, comment,mandatory,fields_in_line," +
        "pos,id_field_depend,confirm_field,confirm_field_title,field_unique,map_provider,css_class, pos_conditional, error_message ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM genatt_entry WHERE id_entry = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE genatt_entry SET " +
        "id_entry=?,id_resource=?,resource_type=?,id_type=?,id_parent=?,title=?,help_message=?," +
        "comment=?,mandatory=?,fields_in_line=?," +
        "pos=?,id_field_depend=?,confirm_field=?,confirm_field_title=?,field_unique=?,map_provider=?,css_class=?, pos_conditional=?, error_message=? WHERE id_entry=?";
    private static final String SQL_QUERY_SELECT_ENTRY_BY_FILTER = SQL_QUERY_SELECT_ENTRY_ATTRIBUTES;
    private static final String SQL_QUERY_SELECT_NUMBER_ENTRY_BY_FILTER = "SELECT COUNT(ent.id_entry) " +
        "FROM genatt_entry ent,genatt_entry_type typ WHERE ent.id_type=typ.id_type ";
    private static final String SQL_QUERY_NEW_POSITION = "SELECT MAX(pos) " +
        "FROM genatt_entry WHERE id_resource=? AND resource_type=?";
    private static final String SQL_QUERY_NEW_POSITION_CONDITIONAL_QUESTION = "SELECT MAX(pos_conditional) FROM genatt_entry WHERE id_field_depend=?";
    private static final String SQL_QUERY_NUMBER_CONDITIONAL_QUESTION = "SELECT COUNT(e2.id_entry) " +
        "FROM genatt_entry e1,genatt_field f1,genatt_entry e2 WHERE e1.id_entry=? AND e1.id_entry=f1.id_entry and e2.id_field_depend=f1.id_field ";
    private static final String SQL_FILTER_ID_RESOURCE = " AND ent.id_resource = ? ";
    private static final String SQL_FILTER_RESOURCE_TYPE = " AND ent.resource_type = ? ";
    private static final String SQL_FILTER_ID_PARENT = " AND ent.id_parent = ? ";
    private static final String SQL_FILTER_ID_PARENT_IS_NULL = " AND ent.id_parent IS NULL ";
    private static final String SQL_FILTER_IS_GROUP = " AND typ.is_group = ? ";
    private static final String SQL_FILTER_IS_COMMENT = " AND typ.is_comment = ? ";
    private static final String SQL_FILTER_ID_FIELD_DEPEND = " AND ent.id_field_depend = ? ";
    private static final String SQL_FILTER_ID_FIELD_DEPEND_IS_NULL = " AND ent.id_field_depend IS NULL ";
    private static final String SQL_FILTER_ID_TYPE = " AND ent.id_type = ? ";
    private static final String SQL_ORDER_BY_POSITION = " ORDER BY ent.pos, ent.pos_conditional ";
    private static final String SQL_GROUP_BY_POSITION = " GROUP BY ent.pos ";
    private static final String SQL_GROUP_BY_ENTRY_ENTRY_TYPE = "GROUP BY ent.id_type,typ.title,typ.is_group,typ.is_comment,typ.class_name,typ.is_mylutece_user," +
        "ent.id_entry,ent.id_resource,ent.resource_type,ent.id_parent,ent.title,ent.help_message,ent.comment,ent.mandatory,ent.fields_in_line," +
        "ent.pos,ent.pos_conditional,ent.id_field_depend,ent.confirm_field,ent.confirm_field_title,ent.field_unique,ent.map_provider,ent.css_class,ent.error_message ";
    private static final String SQL_QUERY_ENTRIES_PARENT_NULL = SQL_QUERY_SELECT_ENTRY_ATTRIBUTES +
        " AND id_parent IS NULL AND id_resource=? AND resource_type = ?" + SQL_FILTER_ID_FIELD_DEPEND_IS_NULL +
        " ORDER BY ent.pos";
    private static final String SQL_QUERY_ENTRY_CONDITIONAL_WITH_ORDER_BY_FIELD = SQL_QUERY_SELECT_ENTRY_ATTRIBUTES +
        " AND pos_conditional = ?  AND ent.id_field_depend = ? AND id_resource=? ";
    private static final String SQL_QUERY_DECREMENT_ORDER_CONDITIONAL = "UPDATE genatt_entry SET pos_conditional = pos_conditional - 1 WHERE pos_conditional > ? AND id_field_depend=? AND id_resource=? AND resource_type=? ";
    private static final int CONSTANT_ZERO = 0;

    /**
     * Insert a new record in the table.
     *
     * @param entry instance of the Entry object to insert
     * @param plugin the plugin
     * @return the id of the new entry
     */
    public synchronized int insert( Entry entry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        entry.setIdEntry( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, entry.getIdEntry(  ) );
        daoUtil.setInt( 2, entry.getIdResource(  ) );
        daoUtil.setString( 3, entry.getResourceType(  ) );
        daoUtil.setInt( 4, entry.getEntryType(  ).getIdType(  ) );

        if ( entry.getParent(  ) != null )
        {
            daoUtil.setInt( 5, entry.getParent(  ).getIdEntry(  ) );
        }
        else
        {
            daoUtil.setIntNull( 5 );
        }

        daoUtil.setString( 6, entry.getTitle(  ) );
        daoUtil.setString( 7, entry.getHelpMessage(  ) );
        daoUtil.setString( 8, entry.getComment(  ) );
        daoUtil.setBoolean( 9, entry.isMandatory(  ) );
        daoUtil.setBoolean( 10, entry.isFieldInLine(  ) );

        daoUtil.setInt( 11, newPosition( entry, plugin ) );

        if ( entry.getFieldDepend(  ) != null )
        {
            daoUtil.setInt( 12, entry.getFieldDepend(  ).getIdField(  ) );
        }
        else
        {
            daoUtil.setIntNull( 12 );
        }

        daoUtil.setBoolean( 13, entry.isConfirmField(  ) );
        daoUtil.setString( 14, entry.getConfirmFieldTitle(  ) );
        daoUtil.setBoolean( 15, entry.isUnique(  ) );

        String strMapProviderKey = ( entry.getMapProvider(  ) == null ) ? StringUtils.EMPTY
                                                                        : entry.getMapProvider(  ).getKey(  );
        daoUtil.setString( 16, strMapProviderKey );
        daoUtil.setString( 17, ( entry.getCSSClass(  ) == null ) ? StringUtils.EMPTY : entry.getCSSClass(  ) );
        daoUtil.setInt( 18, newPositionConditional( entry, plugin ) );
        daoUtil.setString( 19, entry.getErrorMessage(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return entry.getIdEntry(  );
    }

    /**
     * Load the data of the entry from the table
     *
     * @param nId The identifier of the entry
     * @param plugin the plugin
     * @return the instance of the Entry
     */
    public Entry load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        Entry entry = null;

        if ( daoUtil.next(  ) )
        {
            entry = getEntryValues( daoUtil );
        }

        daoUtil.free(  );

        if ( entry != null )
        {
            entry.setNumberConditionalQuestion( numberConditionalQuestion( entry.getIdEntry(  ), plugin ) );
        }

        return entry;
    }

    /**
     * Delete a record from the table
     *
     * @param nIdEntry The identifier of the entry
     * @param plugin the plugin
     */
    public void delete( int nIdEntry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdEntry );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the entry in the table
     *
     * @param entry instance of the Entry object to update
     * @param plugin the plugin
     */
    public void store( Entry entry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nIndex = 1;
        daoUtil.setInt( nIndex++, entry.getIdEntry(  ) );
        daoUtil.setInt( nIndex++, entry.getIdResource(  ) );
        daoUtil.setString( nIndex++, entry.getResourceType(  ) );
        daoUtil.setInt( nIndex++, entry.getEntryType(  ).getIdType(  ) );

        if ( entry.getParent(  ) != null )
        {
            daoUtil.setInt( nIndex++, entry.getParent(  ).getIdEntry(  ) );
        }
        else
        {
            daoUtil.setIntNull( nIndex++ );
        }

        daoUtil.setString( nIndex++, entry.getTitle(  ) );
        daoUtil.setString( nIndex++, entry.getHelpMessage(  ) );
        daoUtil.setString( nIndex++, entry.getComment(  ) );
        daoUtil.setBoolean( nIndex++, entry.isMandatory(  ) );
        daoUtil.setBoolean( nIndex++, entry.isFieldInLine(  ) );

        if ( entry.getFieldDepend(  ) == null )
        {
            daoUtil.setInt( nIndex++, entry.getPosition(  ) );
        }
        else
        {
            daoUtil.setInt( nIndex++, CONSTANT_ZERO );
        }

        if ( entry.getFieldDepend(  ) != null )
        {
            daoUtil.setInt( nIndex++, entry.getFieldDepend(  ).getIdField(  ) );
        }
        else
        {
            daoUtil.setIntNull( nIndex++ );
        }

        daoUtil.setBoolean( nIndex++, entry.isConfirmField(  ) );
        daoUtil.setString( nIndex++, entry.getConfirmFieldTitle(  ) );
        daoUtil.setBoolean( nIndex++, entry.isUnique(  ) );

        String strMapProviderKey = ( entry.getMapProvider(  ) == null ) ? StringUtils.EMPTY
                                                                        : entry.getMapProvider(  ).getKey(  );
        daoUtil.setString( nIndex++, strMapProviderKey );
        daoUtil.setString( nIndex++, ( entry.getCSSClass(  ) == null ) ? StringUtils.EMPTY : entry.getCSSClass(  ) );

        if ( entry.getFieldDepend(  ) != null )
        {
            daoUtil.setInt( nIndex++, entry.getPosition(  ) );
        }
        else
        {
            daoUtil.setInt( nIndex++, CONSTANT_ZERO );
        }

        daoUtil.setString( nIndex++, entry.getErrorMessage(  ) );

        daoUtil.setInt( nIndex++, entry.getIdEntry(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the entry who verify the filter and returns them in
     * a list
     * @param filter the filter
     * @param plugin the plugin
     * @return the list of entry
     */
    public List<Entry> selectEntryListByFilter( EntryFilter filter, Plugin plugin )
    {
        List<Entry> entryList = new ArrayList<Entry>(  );

        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_ENTRY_BY_FILTER );

        sbSQL.append( ( filter.containsIdResource(  ) ) ? SQL_FILTER_ID_RESOURCE : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsResourceType(  ) ) ? SQL_FILTER_RESOURCE_TYPE : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsIdEntryParent(  ) ) ? SQL_FILTER_ID_PARENT : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsEntryParentNull(  ) ) ? SQL_FILTER_ID_PARENT_IS_NULL : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsIdIsGroup(  ) ) ? SQL_FILTER_IS_GROUP : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsIdField(  ) ) ? SQL_FILTER_ID_FIELD_DEPEND : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsFieldDependNull(  ) ) ? SQL_FILTER_ID_FIELD_DEPEND_IS_NULL : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsIdEntryType(  ) ) ? SQL_FILTER_ID_TYPE : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsIdIsComment(  ) ) ? SQL_FILTER_IS_COMMENT : StringUtils.EMPTY );

        sbSQL.append( SQL_GROUP_BY_ENTRY_ENTRY_TYPE );
        sbSQL.append( SQL_ORDER_BY_POSITION );

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString(  ), plugin );
        int nIndex = 1;

        if ( filter.containsIdResource(  ) )
        {
            daoUtil.setInt( nIndex++, filter.getIdResource(  ) );
        }

        if ( filter.containsResourceType(  ) )
        {
            daoUtil.setString( nIndex++, filter.getResourceType(  ) );
        }

        if ( filter.containsIdEntryParent(  ) )
        {
            daoUtil.setInt( nIndex++, filter.getIdEntryParent(  ) );
        }

        if ( filter.containsIdIsGroup(  ) )
        {
            if ( filter.getIdIsGroup(  ) == 0 )
            {
                daoUtil.setBoolean( nIndex++, false );
            }
            else
            {
                daoUtil.setBoolean( nIndex++, true );
            }
        }

        if ( filter.containsIdField(  ) )
        {
            daoUtil.setInt( nIndex++, filter.getIdFieldDepend(  ) );
        }

        if ( filter.containsIdEntryType(  ) )
        {
            daoUtil.setInt( nIndex++, filter.getIdEntryType(  ) );
        }

        if ( filter.containsIdIsComment(  ) )
        {
            if ( filter.getIdIsComment(  ) == 0 )
            {
                daoUtil.setBoolean( nIndex++, false );
            }
            else
            {
                daoUtil.setBoolean( nIndex++, true );
            }
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            entryList.add( getEntryValues( daoUtil ) );
        }

        daoUtil.free(  );

        for ( Entry entryCreated : entryList )
        {
            entryCreated.setNumberConditionalQuestion( numberConditionalQuestion( entryCreated.getIdEntry(  ), plugin ) );
        }

        return entryList;
    }

    /**
     * Return the number of entry who verify the filter
     * @param filter the filter
     * @param plugin the plugin
     * @return the number of entry who verify the filter
     */
    public int selectNumberEntryByFilter( EntryFilter filter, Plugin plugin )
    {
        int nNumberEntry = 0;
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_NUMBER_ENTRY_BY_FILTER );
        sbSQL.append( ( filter.containsIdResource(  ) ) ? SQL_FILTER_ID_RESOURCE : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsIdEntryParent(  ) ) ? SQL_FILTER_ID_PARENT : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsEntryParentNull(  ) ) ? SQL_FILTER_ID_PARENT_IS_NULL : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsIdIsGroup(  ) ) ? SQL_FILTER_IS_GROUP : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsIdIsComment(  ) ) ? SQL_FILTER_IS_COMMENT : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsIdField(  ) ) ? SQL_FILTER_ID_FIELD_DEPEND : StringUtils.EMPTY );
        sbSQL.append( ( filter.containsIdEntryType(  ) ) ? SQL_FILTER_ID_TYPE : StringUtils.EMPTY );

        sbSQL.append( SQL_GROUP_BY_POSITION );
        sbSQL.append( SQL_ORDER_BY_POSITION );

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString(  ), plugin );
        int nIndex = 1;

        if ( filter.containsIdResource(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdResource(  ) );
            nIndex++;
        }

        if ( filter.containsIdEntryParent(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdEntryParent(  ) );
            nIndex++;
        }

        if ( filter.containsIdIsGroup(  ) )
        {
            if ( filter.getIdIsGroup(  ) == 0 )
            {
                daoUtil.setBoolean( nIndex, false );
            }
            else
            {
                daoUtil.setBoolean( nIndex, true );
            }

            nIndex++;
        }

        if ( filter.containsIdIsComment(  ) )
        {
            if ( filter.getIdIsComment(  ) == 0 )
            {
                daoUtil.setBoolean( nIndex, false );
            }
            else
            {
                daoUtil.setBoolean( nIndex, true );
            }

            nIndex++;
        }

        if ( filter.containsIdField(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdFieldDepend(  ) );
            nIndex++;
        }

        if ( filter.containsIdEntryType(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdEntryType(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nNumberEntry = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nNumberEntry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Entry> findEntriesWithoutParent( Plugin plugin, int nIdResource, String strResourceType )
    {
        List<Entry> listResult = new ArrayList<Entry>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ENTRIES_PARENT_NULL, plugin );
        daoUtil.setInt( 1, nIdResource );
        daoUtil.setString( 2, strResourceType );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            listResult.add( getEntryValues( daoUtil ) );
        }

        daoUtil.free(  );

        for ( Entry entryCreated : listResult )
        {
            entryCreated.setNumberConditionalQuestion( numberConditionalQuestion( entryCreated.getIdEntry(  ), plugin ) );
        }

        return listResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entry findByOrderAndIdFieldAndIdResource( Plugin plugin, int nOrder, int nIdField, int nIdResource,
        String strResourceType )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ENTRY_CONDITIONAL_WITH_ORDER_BY_FIELD, plugin );
        daoUtil.setInt( 1, nOrder );
        daoUtil.setInt( 2, nIdField );
        daoUtil.setInt( 3, nIdResource );
        daoUtil.executeQuery(  );

        Entry entry = null;

        if ( daoUtil.next(  ) )
        {
            entry = getEntryValues( daoUtil );
        }

        daoUtil.free(  );

        if ( entry != null )
        {
            entry.setNumberConditionalQuestion( numberConditionalQuestion( entry.getIdEntry(  ), plugin ) );
        }

        return entry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decrementOrderByOne( Plugin plugin, int nOrder, int nIdField, int nIdResource, String strResourceType )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DECREMENT_ORDER_CONDITIONAL, plugin );
        daoUtil.setInt( 1, nOrder );
        daoUtil.setInt( 2, nIdField );
        daoUtil.setInt( 3, nIdResource );
        daoUtil.setString( 4, strResourceType );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Generates a new primary key
     *
     * @param plugin the plugin
     * @return The new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Generates a new entry position
     * @param plugin the plugin
     * @param entry the entry
     * @return the new entry position
     */
    private int newPosition( Entry entry, Plugin plugin )
    {
        DAOUtil daoUtil = null;
        int nPos;

        if ( entry.getFieldDepend(  ) == null )
        {
            daoUtil = new DAOUtil( SQL_QUERY_NEW_POSITION, plugin );

            daoUtil.setInt( 1, entry.getIdResource(  ) );
            daoUtil.setString( 2, entry.getResourceType(  ) );
            daoUtil.executeQuery(  );

            if ( !daoUtil.next(  ) )
            {
                // if the table is empty
                nPos = 1;
            }

            nPos = daoUtil.getInt( 1 ) + 1;
            daoUtil.free(  );
        }
        else
        {
            //case of conditional question only
            nPos = 0;
        }

        return nPos;
    }

    /**
     * Generates a new entry position
     * @param plugin the plugin
     * @param entry the entry
     * @return the new entry position
     */
    private int newPositionConditional( Entry entry, Plugin plugin )
    {
        DAOUtil daoUtil = null;
        int nPos;

        if ( entry.getFieldDepend(  ) != null )
        {
            //case of conditional question only
            daoUtil = new DAOUtil( SQL_QUERY_NEW_POSITION_CONDITIONAL_QUESTION, plugin );

            daoUtil.setInt( 1, entry.getFieldDepend(  ).getIdField(  ) );
            daoUtil.executeQuery(  );

            if ( daoUtil.next(  ) )
            {
                // if the table is empty
                nPos = daoUtil.getInt( 1 ) + 1;
            }
            else
            {
                nPos = 1;
            }

            daoUtil.free(  );
        }
        else
        {
            nPos = 0;
        }

        return nPos;
    }

    /**
     * Return the number of conditional question who are associate to the entry
     * @param nIdEntry the id of the entry
     * @param plugin the plugin
     * @return the number of conditional question
     */
    private int numberConditionalQuestion( int nIdEntry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NUMBER_CONDITIONAL_QUESTION, plugin );
        daoUtil.setInt( 1, nIdEntry );
        daoUtil.executeQuery(  );

        int nNumberConditionalQuestion = 0;

        if ( daoUtil.next(  ) )
        {
            nNumberConditionalQuestion = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nNumberConditionalQuestion;
    }

    /**
     * Get values of an entry from the current row of a daoUtil. The class to
     * daoUtil.next( ) will NOT be made by this method.
     * @param daoUtil The DAOUtil
     * @return The entry, or null if the entry was not found
     */
    private Entry getEntryValues( DAOUtil daoUtil )
    {
        Entry entry;

        int nIndex = 1;
        EntryType entryType = new EntryType(  );
        entryType.setIdType( daoUtil.getInt( nIndex++ ) );
        entryType.setTitle( daoUtil.getString( nIndex++ ) );
        entryType.setGroup( daoUtil.getBoolean( nIndex++ ) );
        entryType.setComment( daoUtil.getBoolean( nIndex++ ) );
        entryType.setBeanName( daoUtil.getString( nIndex++ ) );
        entryType.setMyLuteceUser( daoUtil.getBoolean( nIndex++ ) );

        entry = new Entry(  );

        entry.setEntryType( entryType );
        entry.setIdEntry( daoUtil.getInt( nIndex++ ) );

        entry.setIdResource( daoUtil.getInt( nIndex++ ) );
        entry.setResourceType( daoUtil.getString( nIndex++ ) );

        if ( daoUtil.getObject( nIndex++ ) != null )
        {
            Entry entryParent = new Entry(  );
            entryParent.setIdEntry( daoUtil.getInt( nIndex - 1 ) );
            entry.setParent( entryParent );
        }

        entry.setTitle( daoUtil.getString( nIndex++ ) );
        entry.setHelpMessage( daoUtil.getString( nIndex++ ) );
        entry.setComment( daoUtil.getString( nIndex++ ) );
        entry.setMandatory( daoUtil.getBoolean( nIndex++ ) );
        entry.setFieldInLine( daoUtil.getBoolean( nIndex++ ) );
        entry.setPosition( daoUtil.getInt( nIndex++ ) );

        if ( daoUtil.getObject( nIndex++ ) != null )
        {
            Field fieldDepend = new Field(  );
            fieldDepend.setIdField( daoUtil.getInt( nIndex - 1 ) );
            entry.setFieldDepend( fieldDepend );
        }

        entry.setConfirmField( daoUtil.getBoolean( nIndex++ ) );
        entry.setConfirmFieldTitle( daoUtil.getString( nIndex++ ) );
        entry.setUnique( daoUtil.getBoolean( nIndex++ ) );
        entry.setMapProvider( MapProviderManager.getMapProvider( daoUtil.getString( nIndex++ ) ) );
        entry.setCSSClass( daoUtil.getString( nIndex++ ) );

        if ( daoUtil.getInt( nIndex++ ) > 0 )
        {
            entry.setPosition( daoUtil.getInt( nIndex - 1 ) );
        }

        entry.setErrorMessage( daoUtil.getString( nIndex++ ) );

        return entry;
    }
}